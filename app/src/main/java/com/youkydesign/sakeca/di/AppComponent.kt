package com.youkydesign.sakeca.di

import com.youkydesign.sakeca.MainActivity
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.di.CoreComponent
import com.youkydesign.sakeca.core.di.CoreDependencies
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