package com.youkydesign.recipeapp.di

import com.youkydesign.recipeapp.data.RecipeRepositoryImpl
import com.youkydesign.recipeapp.domain.IRecipeRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {
    @Binds
    abstract fun provideRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): IRecipeRepository

}