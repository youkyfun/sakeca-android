package com.youkydesign.sakeca.core.di

import android.content.Context
import com.youkydesign.sakeca.core.domain.IRecipeRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class, NetworkModule::class, DatabaseModule::class])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    fun provideRepository(): IRecipeRepository
}