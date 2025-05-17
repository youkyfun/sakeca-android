package com.youkydesign.core.di

import com.youkydesign.core.domain.RecipeUseCase
import javax.inject.Singleton

interface RecipeModuleDependencies {
    fun getRecipeUseCase(): RecipeUseCase
}