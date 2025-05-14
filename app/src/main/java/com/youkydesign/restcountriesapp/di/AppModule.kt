package com.youkydesign.restcountriesapp.di

import com.youkydesign.restcountriesapp.domain.RecipeInteractor
import com.youkydesign.restcountriesapp.domain.RecipeUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun provideRecipeUseCase(recipeInteractor: RecipeInteractor): RecipeUseCase
}