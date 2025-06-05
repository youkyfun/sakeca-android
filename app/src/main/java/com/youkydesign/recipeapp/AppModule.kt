package com.youkydesign.recipeapp

import com.youkydesign.core.domain.RecipeInteractor
import com.youkydesign.core.domain.RecipeUseCase
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class AppModule {
    @Binds
    abstract fun provideRecipeUseCase(recipeInteractor: RecipeInteractor): RecipeUseCase
}