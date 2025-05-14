package com.youkydesign.recipeapp.presentation.recipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.youkydesign.recipeapp.RecipeApplication
import com.youkydesign.recipeapp.data.Resource
import com.youkydesign.recipeapp.databinding.FragmentDetailBinding
import com.youkydesign.recipeapp.presentation.ViewModelFactory
import javax.inject.Inject


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory

    private val recipeViewModel: RecipeViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as RecipeApplication).appComponent.inject(this)
    }

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
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Idle -> {
                        showLoading(false)
                    }

                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        Glide.with(binding.imgRecipePhoto).load(resource.data?.imageUrl)
                            .into(binding.imgRecipePhoto)
                        tvRecipeTitle.text = resource.data?.title
                        tvRecipePublisher.text = resource.data?.publisher
                        tvRecipeSocialRank.text = resource.data?.socialRank.toString()
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}