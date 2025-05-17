package com.youkydesign.recipeapp.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.recipeapp.di.AppScope
import com.youkydesign.recipeapp.feature.discovery.ui.discovery.RecipeViewModel
import com.youkydesign.recipeapp.feature.discovery.ui.detail.DetailRecipeViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(private val recipeUseCase: RecipeUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(recipeUseCase) as T
        } else if (modelClass.isAssignableFrom(DetailRecipeViewModel::class.java)) {
            return DetailRecipeViewModel(recipeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}