package com.youkydesign.recipeapp.di

import com.youkydesign.core.di.CoreComponent
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.recipeapp.feature.discovery.ui.DetailFragment
import com.youkydesign.recipeapp.feature.discovery.ui.HomeFragment
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