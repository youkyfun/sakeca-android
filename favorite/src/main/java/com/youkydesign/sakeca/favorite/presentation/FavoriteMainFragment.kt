package com.youkydesign.sakeca.favorite.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.youkydesign.sakeca.core.RecipeSortType
import com.youkydesign.sakeca.core.di.CoreDependenciesProvider
import com.youkydesign.sakeca.core.domain.Recipe
import com.youkydesign.sakeca.favorite.FavoriteViewModelFactory
import com.youkydesign.sakeca.favorite.R
import com.youkydesign.sakeca.favorite.databinding.FragmentFavoriteMainBinding
import com.youkydesign.sakeca.favorite.di.DaggerFavoriteComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteMainFragment : Fragment() {
    private var _binding: FragmentFavoriteMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: FavoriteViewModelFactory

    private lateinit var adapter: FavoriteAdapter

    private val favoriteViewModel: FavoriteRecipeViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

//        val dependencies = (requireActivity().application as SakecaApplication).appComponent
//        DaggerFavoriteComponent.factory().create(requireActivity(), dependencies).inject(this)

        // It should be using the CoreDependenciesProvider pattern:
        val provider = requireActivity().application as? CoreDependenciesProvider // From :core
            ?: throw IllegalStateException("Application must implement CoreDependenciesProvider")
        val coreDependencies = provider.provideCoreDependencies()


        DaggerFavoriteComponent.factory().create(
            context = requireActivity(),
            dependencies = coreDependencies
        ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMainBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = FavoriteAdapter()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteRecipes.layoutManager = layoutManager
        binding.rvFavoriteRecipes.setHasFixedSize(true)

        binding.favTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleOwner.lifecycle.run {
            favoriteViewModel.getFavoriteRecipes()
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

        binding.rvFavoriteRecipes.adapter = adapter
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }
        })
        adapter.addLoadStateListener { loadState ->
            val isEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyData(isEmpty)

            val isRefreshNotLoading = loadState.refresh is LoadState.NotLoading
            showLoading(!isRefreshNotLoading)
        }

        adapter.refresh()
        setFragmentResultListener("itemDeletedKey") { _, _ ->
            favoriteViewModel.getFavoriteRecipes()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteRecipes.collectLatest {
                setRecipeList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (binding.rvFavoriteRecipes.adapter as? FavoriteAdapter)?.setOnItemClickCallback(null)
        binding.rvFavoriteRecipes.adapter = null
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getFavoriteRecipes()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.favoriteContentContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
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
                favoriteViewModel.getFavoriteRecipes(sortType)
                true
            }
            show()
        }
    }

    private fun setRecipeList(favoriteRecipes: PagingData<Recipe>) {
        adapter.submitData(lifecycle, favoriteRecipes)
    }

    private fun showEmptyData(isEmpty: Boolean) {
        binding.favEmptyContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.favoriteContentContainer.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun toRecipeDetail(recipe: Recipe) {
        val bundle = Bundle().run {
            putString("rId", recipe.recipeId)
            this
        }
        findNavController().navigate(R.id.action_favorite_to_recipeDetails, bundle)
    }
}