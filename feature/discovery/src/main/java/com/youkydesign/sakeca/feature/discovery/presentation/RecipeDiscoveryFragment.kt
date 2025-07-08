package com.youkydesign.sakeca.feature.discovery.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.youkydesign.sakeca.core.di.CoreDependenciesProvider
import com.youkydesign.sakeca.core.domain.Recipe
import com.youkydesign.sakeca.core.domain.UiResource
import com.youkydesign.sakeca.designsystem.CustomAssistChip
import com.youkydesign.sakeca.feature.discovery.R
import com.youkydesign.sakeca.feature.discovery.databinding.FragmentDiscoveryRecipeBinding
import com.youkydesign.sakeca.feature.discovery.di.DaggerRecipeDiscoveryComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.youkydesign.sakeca.designsystem.R as designRes

class RecipeDiscoveryFragment : Fragment() {
    private var _binding: FragmentDiscoveryRecipeBinding? = null
    private val binding get() = _binding!!
    private var _searchBarText: String? = null
    private val searchBarText get() = _searchBarText!!

    @Inject
    lateinit var factory: ViewModelFactory

    private val recipeSearchViewModel: RecipeSearchViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val application = requireActivity().application
        if (application !is CoreDependenciesProvider) {
            throw IllegalStateException("Application must implement CoreDependenciesProvider")
        }
        val coreDependencies = application.provideCoreDependencies()

        DaggerRecipeDiscoveryComponent.factory()
            .create(
                context = requireActivity(),
                dependencies = coreDependencies
            )
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.layoutManager = layoutManager // Use binding directly
        binding.rvRecipes.setHasFixedSize(true)

        with(binding) {
            mainTopAppBar.setOnClickListener {
                if (tvCraving.isGone) {
                    tvCraving.visibility = VISIBLE
                }
                if (searchRecommendationContainer.isGone) {
                    searchRecommendationContainer.visibility = VISIBLE
                }
                recipeSearchViewModel.searchRecipes("chicken")
            }
            mainTopAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search_action -> {
                        if (searchBar.isGone) {
                            binding.searchBar.animate().alpha(0f).setDuration(400)
                                .withEndAction {
                                    binding.searchBar.visibility = VISIBLE
                                    binding.searchBar.alpha = 1f
                                }
                        }
                        true
                    }

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
                recipeSearchViewModel.searchRecipes(searchBar.text.toString())

                _searchBarText = binding.searchBar.text.toString()
                binding.tvSectionTitle.text = searchBarText

                false
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBarText = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchRecommendations()

        binding.rvRecipes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 40 && binding.searchBar.isVisible) {
                    binding.searchBar.animate().alpha(0.0f).setDuration(400)
                        .withEndAction { binding.searchBar.visibility = GONE }
                    binding.tvNoRecipe.animate().alpha(0.0f).setDuration(400)
                        .withEndAction { binding.tvNoRecipe.visibility = GONE }
                }
            }
        })

        lifecycleScope.launch {
            recipeSearchViewModel.searchResult.collectLatest { resource ->
                when (resource) {
                    is UiResource.Loading -> showLoading(true)

                    is UiResource.Success -> {
                        showLoading(false)
                        when {
                            resource.data.isNullOrEmpty() -> {
                                binding.searchBar.setText("")
                                binding.rvRecipes.isGone = true
                                binding.tvNoRecipe.isVisible = true

                            }

                            else -> {
                                binding.tvNoRecipe.isGone = true
                                renderRecyclerViewAnimated(resource.data ?: emptyList())
                            }
                        }
                    }

                    is UiResource.Error -> {
                        with(binding) {
                            showLoading(false)
                            tvNoRecipe.isVisible = true

                            tvCraving.isVisible = false
                            tvSectionTitle.isVisible = false
                            rvRecipes.isVisible = false
                            val message = "No recipe found for \"${searchBarText}\""
                            tvNoRecipe.text = message
                            binding.searchBar.setText("")

                            setupSearchRecommendations()
                        }
                        Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is UiResource.Idle -> {
                        binding.tvSectionTitle.text =
                            getString(designRes.string.recommended_recipes)
                        renderRecyclerViewAnimated(resource.data ?: emptyList())
                    }
                }
            }
        }
    }

    private fun renderRecyclerViewAnimated(data: List<Recipe>) {
        binding.rvRecipes.animate().alpha(1f).setDuration(400)
            .withEndAction {
                binding.rvRecipes.visibility = VISIBLE
            }


        setRecipeList(data)
    }

    private fun moveToFavorite() {
        val deepLinkUri = "android-app://com.youkydesign/favorite".toUri()
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)

        val packageManager = requireActivity().packageManager

        val resolveInfoList =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        if (resolveInfoList.isNotEmpty()) {
            val targetPackageName = requireActivity().packageName
            val targetResolveInfo =
                resolveInfoList.find { it.activityInfo.packageName == targetPackageName }

            if (targetResolveInfo != null) {
                intent.setClassName(
                    targetResolveInfo.activityInfo.packageName,
                    targetResolveInfo.activityInfo.name
                )
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        requireContext(),
                        "Favorite feature not available.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Cannot navigate to favorite. Target not found.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "No app can handle this action.", Toast.LENGTH_SHORT)
                .show()
        }
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
        val targetAlpha = if (isLoading) 1f else 0f
        binding.progressBar.animate().alpha(targetAlpha).setDuration(400)
            .withEndAction {
                binding.progressBar.visibility = if (isLoading) VISIBLE else GONE
                binding.homeContent.visibility = if (isLoading) GONE else VISIBLE
            }.start()
        binding.progressBar.visibility = if (isLoading) VISIBLE else binding.progressBar.visibility
    }

    private fun toRecipeDetail(recipe: Recipe) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("android-app://com.youkydesign.recipe/details/${recipe.recipeId}".toUri())
            .build()
        findNavController().navigate(request)
    }

    private fun setupSearchRecommendations() {
        with(binding) {
            if (flexboxChipContainer.isGone) {
                flexboxChipContainer.visibility = VISIBLE
            }
            flexboxChipContainer.removeAllViews()
            resources.getStringArray(designRes.array.search_recommendation)
                .forEachIndexed { index, ingredient ->
                    val chip = CustomAssistChip(requireContext()).apply {
                        id = View.generateViewId() + index
                        setText(ingredient)
                        setChipIcon(
                            ContextCompat.getDrawable(
                                requireContext(),
                                designRes.drawable.arrow_outward
                            )
                        )
                        setOnClickListener {
                            searchBar.setText("")
                            searchView.hide()
                            tvCraving.visibility = GONE
                            tvSectionTitle.text = ingredient.replaceFirstChar { it.uppercase() }
                            tvSectionTitle.visibility = VISIBLE
                            recipeSearchViewModel.searchRecipes(ingredient)
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