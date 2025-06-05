package com.youkydesign.favorite.ui.details

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.youkydesign.core.domain.UiResource
import com.youkydesign.favorite.FavoriteViewModelFactory
import com.youkydesign.favorite.databinding.FragmentFavoriteDetailsBinding
import com.youkydesign.favorite.di.DaggerFavoriteComponent
import com.youkydesign.favorite.ui.main.FavoriteRecipeViewModel
import com.youkydesign.recipeapp.R
import com.youkydesign.recipeapp.RecipeApplication
import com.youkydesign.recipeapp.feature.discovery.IngredientsAdapter
import com.youkydesign.recipeapp.feature.discovery.ui.detail.DetailFragmentArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavoriteDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteDetailsBinding

    private var scrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    private var scrollJob: Job? = null
    private var favoriteRemovalTimer: CountDownTimer? = null
    private var currentSnackbar: Snackbar? = null

    private val fragmentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var lastKnownScrollPosition = 0

    @Inject
    lateinit var factory: FavoriteViewModelFactory

    private val detailRecipeViewModel: FavoriteRecipeViewModel by viewModels {
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
        binding = FragmentFavoriteDetailsBinding.inflate(inflater, container, false)
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

                                // Immediately remove from favorites
                                detailRecipeViewModel.setFavoriteRecipe(resource.data, false)
                                detailRecipeViewModel.getFavoriteRecipes()
                                binding.fabFavorite.setImageResource(R.drawable.favorite_border)

                                currentSnackbar = Snackbar.make(
                                    requireView(),
                                    "Removed from favorite",
                                    Snackbar.LENGTH_LONG
                                )

                                currentSnackbar?.setAction("OK") {
                                    favoriteRemovalTimer?.cancel()
                                }
                                currentSnackbar?.addCallback(object : Snackbar.Callback() {
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_CONSECUTIVE) {
                                            favoriteRemovalTimer?.cancel()
                                        }
                                    }
                                })
                                currentSnackbar?.show()

                                favoriteRemovalTimer =
                                    object : CountDownTimer(3000, 1000) { // 3 seconds
                                        override fun onTick(millisUntilFinished: Long) {}
                                        override fun onFinish() {}
                                    }.start()
                            }
                        } else {
                            binding.fabFavorite.setImageResource(R.drawable.favorite_border)
                            fabFavorite.setOnClickListener {
                                detailRecipeViewModel.setFavoriteRecipe(resource.data, true)
                                detailRecipeViewModel.getFavoriteRecipes()

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
        scrollChangedListener?.let {
            binding.scrollableRecipeDetails.viewTreeObserver.removeOnScrollChangedListener(it)
        }
        lastKnownScrollPosition = 0
        scrollJob?.cancel()
        scrollChangedListener = null
        viewLifecycleOwner.lifecycleScope.cancel()
        fragmentScope.cancel()
    }

    override fun onStop() {
        super.onStop()
        scrollChangedListener?.let {
            binding.scrollableRecipeDetails.viewTreeObserver.removeOnScrollChangedListener(it)
        }
        lastKnownScrollPosition = 0
        scrollJob?.cancel()
        scrollChangedListener = null
        viewLifecycleOwner.lifecycleScope.cancel()
        fragmentScope.cancel()
        scrollJob?.cancel()
    }

    private fun renderTopAppBarAnimated(scrollPosition: Int) {
        val transparentColor =
            ResourcesCompat.getColor(resources, R.color.tw_slate_50_transparent, null)
        val filledColor = ResourcesCompat.getColor(resources, R.color.tw_slate_50, null)

        // This animation should be started with #start()
        val colorAnimationScroll =
            ValueAnimator.ofObject(ArgbEvaluator(), transparentColor, filledColor)
        colorAnimationScroll.duration = 250

        colorAnimationScroll.addUpdateListener { animator ->
            binding.detailAppBarLayout.setBackgroundColor(animator.animatedValue as Int)
        }

        // This animation should be started with #start()
        val colorAnimationTop =
            ValueAnimator.ofObject(ArgbEvaluator(), filledColor, transparentColor)
        colorAnimationTop.duration = 250

        colorAnimationTop.addUpdateListener { animator ->
            binding.detailAppBarLayout.setBackgroundColor(animator.animatedValue as Int)
        }

        // Only run animation if the scroll direction changes relative to the threshold
        val crossedThresholdUpwards = lastKnownScrollPosition >= 860 && scrollPosition < 860
        val crossedThresholdDownwards = lastKnownScrollPosition < 860 && scrollPosition >= 860

        if (crossedThresholdUpwards) {
            if (!colorAnimationTop.isRunning) colorAnimationTop.start()
        } else if (crossedThresholdDownwards) {
            if (!colorAnimationScroll.isRunning) colorAnimationScroll.start()
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
}