package com.youkydesign.restcountriesapp.di

import com.youkydesign.restcountriesapp.data.RecipeRepositoryImpl
import com.youkydesign.restcountriesapp.domain.IRecipeRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {
    @Binds
    abstract fun provideRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): IRecipeRepository

}