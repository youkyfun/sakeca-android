package com.youkydesign.sakeca.core.di

import com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.domain.groceries.GroceriesUseCase

interface CoreDependencies {
    fun getRecipeUseCase(): RecipeUseCase
    fun getGroceriesUseCase(): GroceriesUseCase
}