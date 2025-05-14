package com.youkydesign.restcountriesapp.di

import com.youkydesign.restcountriesapp.presentation.recipe.DetailFragment
import com.youkydesign.restcountriesapp.presentation.recipe.HomeFragment
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