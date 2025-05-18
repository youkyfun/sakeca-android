package com.youkydesign.recipeapp.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.recipeapp.di.AppScope
import com.youkydesign.recipeapp.feature.discovery.ui.detail.DetailRecipeViewModel
import com.youkydesign.recipeapp.feature.discovery.ui.discovery.RecipeViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(private val recipeUseCase: RecipeUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RecipeViewModel::class.java -> RecipeViewModel(recipeUseCase) as T
            DetailRecipeViewModel::class.java -> DetailRecipeViewModel(recipeUseCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}