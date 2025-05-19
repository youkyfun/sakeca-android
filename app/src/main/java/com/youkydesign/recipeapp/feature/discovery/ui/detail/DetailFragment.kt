package com.youkydesign.recipeapp.feature.discovery.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.youkydesign.core.domain.UiResource
import com.youkydesign.recipeapp.R
import com.youkydesign.recipeapp.RecipeApplication
import com.youkydesign.recipeapp.databinding.FragmentDetailBinding
import com.youkydesign.recipeapp.feature.discovery.IngredientsAdapter
import com.youkydesign.recipeapp.feature.discovery.ViewModelFactory
import javax.inject.Inject

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory

    private val detailRecipeViewModel: DetailRecipeViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as RecipeApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.detailTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredients.layoutManager = layoutManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rId = DetailFragmentArgs.fromBundle(arguments as Bundle).rId
        detailRecipeViewModel.getRecipe(rId)

        with(binding) {
            detailRecipeViewModel.recipeDetailState.observe(viewLifecycleOwner) { resource ->
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
                        if (resource.data == null) {
                            Toast.makeText(
                                requireContext(),
                                "Sorry, something went wrong! We can't get this recipe right now.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Glide.with(binding.imgRecipePhoto).load(resource.data?.imageUrl)
                            .into(binding.imgRecipePhoto)
                        tvRecipeTitle.text = resource.data?.title
                        tvRecipePublisher.text = resource.data?.publisher
                        tvRecipeSocialRank.text = resource.data?.socialRank.toString()

                        setIngredientList(resource.data?.ingredients ?: emptyList())

                        if (resource.data?.isFavorite != null && resource.data?.isFavorite == true) {
                            binding.fabFavorite.setImageResource(R.drawable.favorite)
                            fabFavorite.setOnClickListener {
                                detailRecipeViewModel.setFavoriteRecipe(resource.data, false)
                                Snackbar.make(
                                    requireView(),
                                    "Removed from favorite",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                binding.fabFavorite.setImageResource(R.drawable.favorite_border)
                            }
                        } else {
                            binding.fabFavorite.setImageResource(R.drawable.favorite_border)
                            fabFavorite.setOnClickListener {
                                detailRecipeViewModel.setFavoriteRecipe(resource.data, true)
                                Snackbar.make(
                                    requireView(),
                                    "Added to favorite",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                binding.fabFavorite.setImageResource(R.drawable.favorite)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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