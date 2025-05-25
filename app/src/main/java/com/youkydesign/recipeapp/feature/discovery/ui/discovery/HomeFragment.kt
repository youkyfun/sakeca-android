package com.youkydesign.recipeapp.feature.discovery.ui.discovery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
                                "Module not installed: $e",
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

                val searchQuery = binding.searchBar.text
                val title = "Search result for $searchQuery"
                binding.tvSectionTitle.text = title

                false
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeViewModel.searchResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is UiResource.Loading -> showLoading(true)

                is UiResource.Success -> {
                    showLoading(false)
                    when {
                        resource.data.isNullOrEmpty() -> {
                            binding.searchBar.setText("")
                            binding.rvRecipes.isGone = true
                            binding.tvNoRecipe.isVisible = true

                            setupSearchRecommendations()
                        }

                        else -> {
                            binding.rvRecipes.isVisible = true
                            binding.tvNoRecipe.isGone = true
                            binding.searchRecommendationContainer.isVisible = false
                            binding.searchBar.setText("")

                            setRecipeList(resource.data ?: emptyList())
                        }
                    }
                }

                is UiResource.Error -> {
                    with(binding) {
                        showLoading(false)
                        tvNoRecipe.isVisible = true

                        tvSectionTitle.text = getString(R.string.recommended_recipes)
                        val message = "No recipe found for \"${searchBar.text}\""
                        tvNoRecipe.text = message
                        searchBar.setText("")

                        setupSearchRecommendations()

                        ConstraintSet().apply {
                            clone(mainContainer)
                            connect(
                                searchRecommendationContainer.id,
                                ConstraintSet.TOP,
                                tvNoRecipe.id,
                                ConstraintSet.BOTTOM
                            )
                            connect(
                                tvSectionTitle.id,
                                ConstraintSet.TOP,
                                searchRecommendationContainer.id,
                                ConstraintSet.BOTTOM
                            )
                            connect(
                                rvRecipes.id,
                                ConstraintSet.TOP,
                                tvSectionTitle.id,
                                ConstraintSet.BOTTOM
                            )
                            applyTo(mainContainer)
                        }
                    }
                    Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show()
                }

                is UiResource.Idle -> {
                    binding.searchBar.setText("")
                    binding.tvSectionTitle.text = getString(R.string.recommended_recipes)
                }
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

    private fun setupSearchRecommendations() {
        with(binding) {
            searchRecommendationContainer.isVisible = true

            flexboxChipContainer.removeAllViews()
            resources.getStringArray(R.array.search_recommendation)
                .forEachIndexed { index, item ->
                    val chip = CustomAssistChip(requireContext()).apply {
                        id = View.generateViewId() + index
                        setText(item)
                        setChipIcon(
                            ContextCompat.getDrawable(requireContext(), R.drawable.arrow_outward)
                        )
                        setOnClickListener {
                            val title = "Search result for $item"
                            binding.tvSectionTitle.text = title

                            searchBar.setText("")
                            searchView.hide()
                            recipeViewModel.searchRecipes(item)
                        }
                        layoutParams = ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply { setMargins(8, 8, 8, 8) }
                    }
                    flexboxChipContainer.addView(chip)
                }
        }
    }
}