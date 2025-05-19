package com.youkydesign.recipeapp.feature.discovery.ui.discovery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.UiResource
import com.youkydesign.recipeapp.R
import com.youkydesign.recipeapp.RecipeApplication
import com.youkydesign.recipeapp.databinding.FragmentHomeBinding
import com.youkydesign.recipeapp.feature.discovery.RecipeAdapter
import com.youkydesign.recipeapp.feature.discovery.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.layoutManager = layoutManager
        binding.rvRecipes.setHasFixedSize(true)

        with(binding) {
            searchBar.inflateMenu(R.menu.app_menu)
            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorite_action -> {
                        try {
                            installFavoriteModule()
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "Module not installed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }

                    else -> false
                }
            }

            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                recipeViewModel.searchRecipes(searchBar.text.toString())
                false
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeViewModel.searchResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is UiResource.Loading -> {
                    showLoading(true)
                }

                is UiResource.Success -> {
                    showLoading(false)
                    if (resource.data.isNullOrEmpty()) {
                        binding.rvRecipes.visibility = View.GONE
                        binding.tvNoRecipe.visibility = View.VISIBLE
                        return@observe
                    }
                    binding.tvNoRecipe.visibility = View.GONE
                    setRecipeList(resource.data ?: emptyList())
                }

                is UiResource.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Can't load data", Toast.LENGTH_SHORT).show()
                }

                is UiResource.Idle -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToFavorite() {
        findNavController().navigate(R.id.action_homeFragment_to_favorite_nav_graph)
    }

    private fun setRecipeList(recipeList: List<Recipe>) {
        val adapter = RecipeAdapter(recipeList)
        binding.rvRecipes.adapter = adapter

        adapter.setOnItemClickCallback(object : RecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }

        })
    }

    private fun installFavoriteModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(requireActivity())
        val moduleFavorite = "favorite"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            moveToFavorite()
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleFavorite)
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Success installing module",
                        Toast.LENGTH_SHORT
                    ).show()
                    moveToFavorite()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error installing module", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun toRecipeDetail(recipe: Recipe) {
        val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        toDetailFragment.rId = recipe.recipeId
        view?.findNavController()?.navigate(toDetailFragment)
    }
}