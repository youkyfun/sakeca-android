package com.youkydesign.core.di

import com.youkydesign.core.RecipeRepositoryImpl
import com.youkydesign.core.data.local.LocalRecipeDataSource
import com.youkydesign.core.data.network.NetworkRecipeDataSource
import com.youkydesign.core.domain.IRecipeRepository
import com.youkydesign.core.domain.RecipeInteractor
import com.youkydesign.core.domain.RecipeUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class, NetworkModule::class])
class CoreModule {
    @Provides
    fun provideRecipeUseCase(recipeRepositoryImpl: RecipeRepositoryImpl): RecipeUseCase =
        RecipeInteractor(
            recipeRepository = recipeRepositoryImpl
        )

    @Provides
    @Singleton
    fun provideRecipeRepository(
        localDataSource: LocalRecipeDataSource,
        networkDataSource: NetworkRecipeDataSource
    ): IRecipeRepository = RecipeRepositoryImpl(localDataSource, networkDataSource)

}