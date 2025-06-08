package com.youkydesign.sakeca.core.di

import com.youkydesign.sakeca.core.domain.RecipeUseCase

interface CoreDependencies {
    fun getRecipeUseCase(): RecipeUseCase
}