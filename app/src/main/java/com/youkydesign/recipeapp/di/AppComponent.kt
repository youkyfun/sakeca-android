package com.youkydesign.recipeapp.di

import com.youkydesign.core.di.AppScope
import com.youkydesign.core.di.CoreComponent
import com.youkydesign.core.di.CoreDependencies
import com.youkydesign.recipeapp.feature.discovery.ui.discovery.HomeFragment
import dagger.Component

@AppScope
@Component(dependencies = [CoreComponent::class], modules = [AppModule::class])
interface AppComponent : CoreDependencies {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: HomeFragment)
}