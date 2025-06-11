package com.youkydesign.sakeca.di

import com.youkydesign.sakeca.core.domain.RecipeInteractor
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.domain.groceries.GroceriesInteractor
import com.youkydesign.sakeca.domain.groceries.GroceriesUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun provideRecipeUseCase(interactor: RecipeInteractor): RecipeUseCase

    @Binds
    abstract fun provideGroceriesUseCase(interactor: GroceriesInteractor): GroceriesUseCase
}