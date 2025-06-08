package com.youkydesign.recipeapp.di

import com.youkydesign.core.di.AppScope
import com.youkydesign.core.di.CoreComponent
import com.youkydesign.core.di.CoreDependencies
import com.youkydesign.recipeapp.MainActivity
import dagger.Component

@AppScope
@Component(dependencies = [CoreComponent::class], modules = [AppModule::class])
interface AppComponent : CoreDependencies {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }
}