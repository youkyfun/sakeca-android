@file:Suppress("RedundantSuppression", "unused")

package com.youkydesign.feature.details.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.youkydesign.core.di.CoreDependenciesProvider
import com.youkydesign.core.domain.UiResource
import com.youkydesign.feature.details.databinding.FragmentRecipeDetailsBinding
import com.youkydesign.feature.details.di.DaggerRecipeDetailsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeDetailsFragment : Fragment() {
    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!


    private var scrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    private var scrollJob: Job? = null

    private val fragmentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var lastKnownScrollPosition = 0

    @Inject
    lateinit var factory: ViewModelFactory

    private val detailRecipeViewModel: DetailRecipeViewModel by viewModels<DetailRecipeViewModel> {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val application = requireActivity().application
        if (application !is CoreDependenciesProvider) {
            throw IllegalStateException("Application must implement CoreDependenciesProvider")
        }
        val coreDependencies = application.provideCoreDependencies()

        // Now inject using DaggerRecipeDetailsComponent
        DaggerRecipeDetailsComponent.factory()
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
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.detailTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredients.layoutManager = layoutManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rId = RecipeDetailsFragmentArgs.fromBundle(arguments as Bundle).rId
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
                        TITLE_SCREEN = resource.data?.title ?: TITLE_SCREEN
                        tvRecipeTitle.text = resource.data?.title
                        tvRecipePublisher.text = resource.data?.publisher
                        tvRecipeSocialRank.text = resource.data?.socialRank.toString()

                        setIngredientList(resource.data?.ingredients ?: emptyList())

                        if (resource.data?.isFavorite != null && resource.data?.isFavorite == true) {
                            binding.fabFavorite.setImageResource(com.youkydesign.designsystem.R.drawable.favorite)
                            fabFavorite.setOnClickListener {
                                detailRecipeViewModel.setFavoriteRecipe(resource.data, false)
                                Snackbar.make(
                                    requireView(),
                                    "Removed from favorite",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                binding.fabFavorite.setImageResource(com.youkydesign.designsystem.R.drawable.favorite_border)
                            }
                        } else {
                            binding.fabFavorite.setImageResource(com.youkydesign.designsystem.R.drawable.favorite_border)
                            fabFavorite.setOnClickListener {
                                detailRecipeViewModel.setFavoriteRecipe(resource.data, true)
                                Snackbar.make(
                                    requireView(),
                                    "Added to favorite",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                binding.fabFavorite.setImageResource(com.youkydesign.designsystem.R.drawable.favorite)
                            }
                        }
                    }
                }
            }

            scrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
                scrollJob?.cancel()
                scrollJob = fragmentScope.launch {
                    val scrollY: Int = scrollableRecipeDetails.scrollY
                    renderTopAppBarAnimated(scrollY)
                }
            }
            scrollableRecipeDetails.viewTreeObserver.addOnScrollChangedListener(
                scrollChangedListener
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanUpResources()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        cleanUpScrollRelatedResources()
    }

    private fun cleanUpScrollRelatedResources() {
        scrollChangedListener?.let {
            _binding?.scrollableRecipeDetails?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
        lastKnownScrollPosition = 0
        scrollJob?.cancel()
        scrollChangedListener = null
    }

    private fun cleanUpResources() {
        cleanUpScrollRelatedResources()
        fragmentScope.cancel()
    }

    private fun renderTopAppBarAnimated(scrollPosition: Int) {

        val slate50Color = ResourcesCompat.getColor(
            resources,
            com.youkydesign.designsystem.R.color.tw_slate_50,
            null
        )
        val slate400Color = ResourcesCompat.getColor(
            resources,
            com.youkydesign.designsystem.R.color.tw_slate_400,
            null
        )
        val slate950Color = ResourcesCompat.getColor(
            resources,
            com.youkydesign.designsystem.R.color.tw_slate_950,
            null
        )
        val transparentColor =
            ResourcesCompat.getColor(
                resources,
                com.youkydesign.designsystem.R.color.tw_slate_50_transparent,
                null
            )
        val filledColor = ResourcesCompat.getColor(
            resources,
            com.youkydesign.designsystem.R.color.tw_slate_50,
            null
        )

        // This animation should be started with #start()
        val colorAnimationScroll =
            ValueAnimator.ofObject(ArgbEvaluator(), transparentColor, filledColor)
        colorAnimationScroll.duration = 250

        colorAnimationScroll.addUpdateListener { animator ->
            binding.detailTopAppBar.setNavigationIconTint(slate400Color)
            binding.detailTopAppBar.setTitleTextColor(slate950Color)
            binding.detailAppBarLayout.setBackgroundColor(animator.animatedValue as Int)
            binding.detailTopAppBar.setTitle(TITLE_SCREEN)
        }

        // This animation should be started with #start()
        val colorAnimationTop =
            ValueAnimator.ofObject(ArgbEvaluator(), filledColor, transparentColor)
        colorAnimationTop.duration = 250

        colorAnimationTop.addUpdateListener { animator ->
            binding.detailTopAppBar.setTitleTextColor(slate50Color)
            binding.detailTopAppBar.setNavigationIconTint(slate50Color)
            binding.detailAppBarLayout.setBackgroundColor(animator.animatedValue as Int)
            binding.detailTopAppBar.setTitle(null)
        }

        // Only run animation if the scroll direction changes relative to the threshold
        val crossedThresholdUpwards = lastKnownScrollPosition >= 860 && scrollPosition < 860
        val crossedThresholdDownwards = lastKnownScrollPosition < 860 && scrollPosition >= 860

        if (crossedThresholdUpwards) {
            if (!colorAnimationTop.isRunning) colorAnimationTop.start()
        } else if (crossedThresholdDownwards) {
            if (!colorAnimationScroll.isRunning) {
                colorAnimationScroll.start()
            }
        }

        lastKnownScrollPosition = scrollPosition
    }

    private fun setIngredientList(ingredientList: List<String>) {
        val adapter = IngredientsAdapter(ingredientList)
        binding.rvIngredients.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.scrollviewChildContainer.visibility =
            if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("unused")
    companion object {
        var TITLE_SCREEN = "Details"
    }
}