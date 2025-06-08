package com.youkydesign.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.favorite.presentation.FavoriteRecipeViewModel
import com.youkydesign.core.di.AppScope
import javax.inject.Inject

@AppScope
class FavoriteViewModelFactory @Inject constructor(private val recipeUseCase: RecipeUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            FavoriteRecipeViewModel::class.java -> return FavoriteRecipeViewModel(recipeUseCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}