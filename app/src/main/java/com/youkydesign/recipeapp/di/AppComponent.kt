package com.youkydesign.recipeapp.di

import com.youkydesign.core.di.CoreComponent
import com.youkydesign.core.di.FavoriteModuleDependencies
import com.youkydesign.recipeapp.feature.discovery.ui.detail.DetailFragment
import com.youkydesign.recipeapp.feature.discovery.ui.discovery.HomeFragment
import dagger.Component

@AppScope
@Component(dependencies = [CoreComponent::class], modules = [AppModule::class])
interface AppComponent : FavoriteModuleDependencies {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: HomeFragment)
    fun inject(fragment: DetailFragment)
}