package com.youkydesign.sakeca.feature.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.domain.groceries.GroceriesUseCase
import javax.inject.Inject

@AppScope
class DetailsViewModelFactory @Inject constructor(
    private val recipeUseCase: RecipeUseCase,
    private val groceriesUseCase: GroceriesUseCase
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DetailRecipeViewModel::class.java -> DetailRecipeViewModel(
                recipeUseCase = recipeUseCase,
                groceryUseCase = groceriesUseCase
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}