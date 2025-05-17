package com.youkydesign.core.di

import com.youkydesign.core.RecipeRepositoryImpl
import com.youkydesign.core.domain.RecipeInteractor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(): CoreComponent
    }

    fun inject(recipeInteractor: RecipeInteractor)
    fun inject(recipeRepositoryImpl: RecipeRepositoryImpl)
}