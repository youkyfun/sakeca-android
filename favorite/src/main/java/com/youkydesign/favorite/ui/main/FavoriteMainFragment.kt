package com.youkydesign.favorite.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.UiResource
import com.youkydesign.favorite.FavoriteViewModelFactory
import com.youkydesign.favorite.R
import com.youkydesign.favorite.databinding.FragmentFavoriteMainBinding
import com.youkydesign.favorite.di.DaggerFavoriteComponent
import com.youkydesign.recipeapp.RecipeApplication
import javax.inject.Inject

class FavoriteMainFragment : Fragment() {
    private var _binding: FragmentFavoriteMainBinding? = null
    private val binding get() = _binding!!

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

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(
            menu: android.view.Menu,
            menuInflater: android.view.MenuInflater
        ) {
            menuInflater.inflate(R.menu.favorite_app_bar_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: android.view.MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.sort_by_date -> {}
                R.id.sort_by_recipe_name -> {}
            }
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMainBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteRecipes.layoutManager = layoutManager
        binding.rvFavoriteRecipes.setHasFixedSize(true)

        binding.favTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteViewModel.favoriteRecipes.observe(viewLifecycleOwner) { resource ->
            if (_binding != null) {
                when (resource) {
                    is UiResource.Loading -> {
                        showLoading(true)
                    }

                    is UiResource.Error -> {
                        showLoading(false)
                        Toast.makeText(requireActivity(), "Can't load data", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is UiResource.Idle -> {
                        binding.favEmptyContainer.visibility = View.VISIBLE
                    }

                    is UiResource.Success -> {
                        showLoading(false)
                        when {
                            resource.data.isNullOrEmpty() -> {
                                binding.rvFavoriteRecipes.isGone = true
                                binding.favEmptyContainer.isVisible = true
                                return@observe
                            }

                            else -> {
                                binding.rvFavoriteRecipes.isVisible = true
                                binding.favEmptyContainer.isGone = true
                                setRecipeList(
                                    resource.data ?: emptyList()
                                )
                            }
                        }
                        binding.favEmptyContainer.visibility = View.GONE
                        binding.rvFavoriteRecipes.visibility = View.VISIBLE
                        setRecipeList(resource.data ?: emptyList())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
        val adapter = FavoriteAdapter(recipeList)
        binding.rvFavoriteRecipes.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }

        })
    }

    private fun toRecipeDetail(recipe: Recipe) {
        val toDetailFragment =
            FavoriteMainFragmentDirections.actionFavoriteMainFragmentToFavoriteDetailsFragment()
        toDetailFragment.rId = recipe.recipeId
        view?.findNavController()?.navigate(toDetailFragment)
    }

}