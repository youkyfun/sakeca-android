package com.youkydesign.sakeca.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.favorite.presentation.FavoriteRecipeViewModel
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