package com.youkydesign.recipeapp.di

import com.youkydesign.recipeapp.domain.RecipeInteractor
import com.youkydesign.recipeapp.domain.RecipeUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun provideRecipeUseCase(recipeInteractor: RecipeInteractor): RecipeUseCase
}