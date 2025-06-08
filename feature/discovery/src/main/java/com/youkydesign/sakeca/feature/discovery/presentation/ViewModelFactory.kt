package com.youkydesign.sakeca.feature.discovery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(private val recipeUseCase: RecipeUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RecipeSearchViewModel::class.java -> RecipeSearchViewModel(recipeUseCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}