package com.youkydesign.sakeca.di

import com.youkydesign.sakeca.core.domain.RecipeInteractor
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class AppModule {
    @Binds
    abstract fun provideRecipeUseCase(recipeInteractor: RecipeInteractor): RecipeUseCase
}