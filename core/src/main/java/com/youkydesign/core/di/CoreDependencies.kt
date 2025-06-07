package com.youkydesign.core.di

import com.youkydesign.core.domain.RecipeUseCase

interface CoreDependencies {
    fun getRecipeUseCase(): RecipeUseCase
}