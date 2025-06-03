package com.youkydesign.favorite.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.domain.Recipe
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

        binding.favTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_action -> {
                    showSortingMenu()
                    true
                }

                else -> false
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteViewModel.favoriteRecipes.observe(viewLifecycleOwner) {
            showLoading(true)
            setRecipeList(it)
            showLoading(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingShimmer.visibility = View.VISIBLE
        } else {
            binding.loadingShimmer.visibility = View.GONE
        }
    }

    private fun showSortingMenu() {
        val view = requireActivity().findViewById<View>(R.id.sort_action) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.sort_favorite, menu)

            setOnMenuItemClickListener {
                val (sortTextRes, sortType) = when (it.itemId) {
                    R.id.sort_date_asc -> R.string.sort_favorite_by_oldest to RecipeSortType.BY_DATE_ASC
                    R.id.sort_date_desc -> R.string.sort_favorite_by_latest to RecipeSortType.BY_DATE_DESC
                    R.id.sort_name -> R.string.sort_favorite_by_name to RecipeSortType.BY_NAME
                    else -> R.string.sort_favorite_by_latest to RecipeSortType.BY_DATE_DESC
                }

                binding.tvFavoriteBy.text = requireContext().getString(sortTextRes)
                favoriteViewModel.filter(sortType)
                true
            }
            show()
        }
    }

    private fun setRecipeList(favoriteRecipes: PagingData<Recipe>) {
        val adapter = FavoriteAdapter()
        binding.rvFavoriteRecipes.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }
        })

        adapter.submitData(lifecycle, favoriteRecipes)
        adapter.addLoadStateListener { loadState ->
            val isEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyData(isEmpty)
        }
    }

    private fun showEmptyData(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvFavoriteBy.visibility = View.GONE
            binding.favEmptyContainer.visibility = View.VISIBLE
            binding.rvFavoriteRecipes.visibility = View.GONE
        } else {
            binding.tvFavoriteBy.visibility = View.VISIBLE
            binding.favEmptyContainer.visibility = View.GONE
            binding.rvFavoriteRecipes.visibility = View.VISIBLE
        }
    }

    private fun toRecipeDetail(recipe: Recipe) {
        val toDetailFragment =
            FavoriteMainFragmentDirections.actionFavoriteMainFragmentToFavoriteDetailsFragment()
        toDetailFragment.rId = recipe.recipeId
        view?.findNavController()?.navigate(toDetailFragment)
    }
}