package com.youkydesign.restcountriesapp.di

import android.content.Context
import com.youkydesign.restcountriesapp.domain.IRecipeRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class, DatabaseModule::class])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    fun provideRepository(): IRecipeRepository
}