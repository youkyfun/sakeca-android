package com.youkydesign.core.di

import com.youkydesign.core.domain.RecipeUseCase

interface CoreModuleDependencies {
    fun provideRecipeUseCase(): RecipeUseCase
}