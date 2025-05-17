package com.youkydesign.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.recipeapp.di.AppScope
import javax.inject.Inject

@AppScope
class FavoriteViewModelFactory @Inject constructor(private val recipeUseCase: RecipeUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteRecipeViewModel::class.java)) {
            return FavoriteRecipeViewModel(recipeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}