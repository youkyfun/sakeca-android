package com.youkydesign.recipeapp

import android.app.Application
import com.youkydesign.recipeapp.di.AppComponent
import com.youkydesign.recipeapp.di.CoreComponent
import com.youkydesign.recipeapp.di.DaggerAppComponent
import com.youkydesign.recipeapp.di.DaggerCoreComponent

open class RecipeApplication : Application() {
    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}