package com.youkydesign.sakeca

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.youkydesign.sakeca.core.di.CoreComponent
import com.youkydesign.sakeca.core.di.CoreDependencies
import com.youkydesign.sakeca.core.di.CoreDependenciesProvider
import com.youkydesign.sakeca.core.di.DaggerCoreComponent
import com.youkydesign.sakeca.di.AppComponent
import com.youkydesign.sakeca.di.DaggerAppComponent

open class SakecaApplication : Application(), CoreDependenciesProvider {
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

    override fun provideCoreDependencies(): CoreDependencies = appComponent
}