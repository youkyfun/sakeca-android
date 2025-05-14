package com.youkydesign.recipeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.recipeapp.di.AppScope
import com.youkydesign.recipeapp.domain.RecipeUseCase
import com.youkydesign.recipeapp.presentation.recipe.RecipeViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(private val recipeUseCase: RecipeUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(recipeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}