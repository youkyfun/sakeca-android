package com.youkydesign.discovery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youkydesign.discovery.IngredientsAdapter
import com.youkydesign.discovery.ViewModelFactory
import com.youkydesign.discovery.databinding.FragmentDetailBinding
import com.youkydesign.core.domain.UiResource
import javax.inject.Inject


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvIngredients: RecyclerView

    @Inject
    lateinit var factory: ViewModelFactory

    private val recipeViewModel: RecipeViewModel by viewModels {
        factory
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        (requireActivity().application as RecipeApplication).appComponent.inject(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        recipeViewModel.getRecipe(arguments?.getString("rid").toString())

        with(binding) {
            recipeViewModel.recipeDetailState.observe(viewLifecycleOwner) { resource ->

                when (resource) {
                    is UiResource.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is UiResource.Idle -> {
                        showLoading(false)
                    }

                    is UiResource.Loading -> {
                        showLoading(true)
                    }

                    is UiResource.Success -> {
                        showLoading(false)
                        Glide.with(binding.imgRecipePhoto).load(resource.data?.imageUrl)
                            .into(binding.imgRecipePhoto)
                        tvRecipeTitle.text = resource.data?.title
                        tvRecipePublisher.text = resource.data?.publisher
                        tvRecipeSocialRank.text = resource.data?.socialRank.toString()
                        setIngredientList(resource.data!!.ingredients)
                    }
                }

            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setIngredientList(ingredientList: List<String>) {
        val adapter = IngredientsAdapter(ingredientList)
        binding.rvIngredients.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}