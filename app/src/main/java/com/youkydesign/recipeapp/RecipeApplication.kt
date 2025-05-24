package com.youkydesign.recipeapp

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.youkydesign.core.di.CoreComponent
import com.youkydesign.core.di.DaggerCoreComponent
import com.youkydesign.recipeapp.di.AppComponent
import com.youkydesign.recipeapp.di.DaggerAppComponent

open class RecipeApplication : Application() {
    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}