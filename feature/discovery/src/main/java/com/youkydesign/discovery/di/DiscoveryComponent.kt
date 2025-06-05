package com.youkydesign.discovery.di

import android.content.Context
import com.youkydesign.core.di.CoreModuleDependencies
import com.youkydesign.discovery.DiscoveryActivity
import com.youkydesign.discovery.DiscoveryFragment
import com.youkydesign.recipeapp.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreModuleDependencies::class])
interface DiscoveryComponent {

    fun inject(activity: DiscoveryActivity)
    fun inject(fragment: DiscoveryFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreModuleDependencies
        ): DiscoveryComponent

    }
}
