package com.youkydesign.recipeapp

import com.youkydesign.core.di.CoreComponent
import com.youkydesign.core.di.CoreModuleDependencies
import com.youkydesign.core.di.FavoriteModuleDependencies
import dagger.Component

@AppScope
@Component(dependencies = [CoreComponent::class], modules = [AppModule::class])
interface AppComponent : FavoriteModuleDependencies, CoreModuleDependencies {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }
}