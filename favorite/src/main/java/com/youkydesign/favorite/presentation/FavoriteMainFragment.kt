package com.youkydesign.favorite.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.UiResource
import com.youkydesign.favorite.FavoriteViewModelFactory
import com.youkydesign.favorite.databinding.FragmentFavoriteMainBinding
import com.youkydesign.favorite.di.DaggerFavoriteComponent
import com.youkydesign.recipeapp.RecipeApplication
import com.youkydesign.recipeapp.feature.discovery.RecipeAdapter
import javax.inject.Inject

class FavoriteMainFragment : Fragment() {
    private var _binding: FragmentFavoriteMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvFavoriteRecipes: RecyclerView

    @Inject
    lateinit var factory: FavoriteViewModelFactory

    private val favoriteViewModel: FavoriteRecipeViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val dependencies = (requireActivity().application as RecipeApplication).appComponent
        DaggerFavoriteComponent.factory().create(requireActivity(), dependencies).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteMainBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteRecipes.layoutManager = layoutManager

        rvFavoriteRecipes = binding.rvFavoriteRecipes
        rvFavoriteRecipes.setHasFixedSize(true)

        binding.favTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteViewModel.favoriteRecipes.observe(requireActivity()) { resource ->
            when (resource) {
                is UiResource.Loading -> {
                    showLoading(true)
                }

                is UiResource.Error -> {
                    showLoading(false)
                    Toast.makeText(requireActivity(), "Can't load data", Toast.LENGTH_SHORT)
                        .show()
                }

                is UiResource.Idle -> {}
                is UiResource.Success -> {
                    showLoading(false)
                    if (resource.data.isNullOrEmpty()) {
                        binding.rvFavoriteRecipes.visibility = View.VISIBLE
                        binding.tvNoFavorite.visibility = View.VISIBLE
                        return@observe
                    }
                    if (resource.data != null) {
                        binding.tvNoFavorite.visibility = View.GONE
                        binding.rvFavoriteRecipes.visibility = View.VISIBLE
                        setRecipeList(resource.data!!)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbFavorite.visibility = View.VISIBLE
        } else {
            binding.pbFavorite.visibility = View.GONE
        }
    }

    private fun setRecipeList(recipeList: List<Recipe>) {
        val adapter = RecipeAdapter(recipeList)
        binding.rvFavoriteRecipes.adapter = adapter

        adapter.setOnItemClickCallback(object : RecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }

        })
    }

    private fun toRecipeDetail(recipe: Recipe) {
        val toDetailFragment =
            FavoriteMainFragmentDirections.actionFavoriteToDetail()
        toDetailFragment.rId = recipe.recipeId
        view?.findNavController()?.navigate(toDetailFragment)
    }

}