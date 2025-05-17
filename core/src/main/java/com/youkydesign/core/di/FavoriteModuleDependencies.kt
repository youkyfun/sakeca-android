package com.youkydesign.core.di

import com.youkydesign.core.domain.RecipeUseCase

interface FavoriteModuleDependencies {
    fun getRecipeUseCase(): RecipeUseCase
}