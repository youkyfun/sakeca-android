package com.youkydesign.restcountriesapp

import android.app.Application
import com.youkydesign.restcountriesapp.di.AppComponent
import com.youkydesign.restcountriesapp.di.CoreComponent
import com.youkydesign.restcountriesapp.di.DaggerAppComponent
import com.youkydesign.restcountriesapp.di.DaggerCoreComponent

open class RecipeApplication : Application() {
    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}