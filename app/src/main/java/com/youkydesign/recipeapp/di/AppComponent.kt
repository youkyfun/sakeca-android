package com.youkydesign.recipeapp.di

import com.youkydesign.recipeapp.presentation.recipe.DetailFragment
import com.youkydesign.recipeapp.presentation.recipe.HomeFragment
import dagger.Component

@AppScope
@Component(dependencies = [CoreComponent::class], modules = [AppModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: HomeFragment)
    fun inject(fragment: DetailFragment)
}