package com.youkydesign.feature.discovery.di

import android.content.Context
import com.youkydesign.core.di.AppScope
import com.youkydesign.core.di.CoreDependencies
import com.youkydesign.feature.discovery.presentation.RecipeDiscoveryActivity
import com.youkydesign.feature.discovery.presentation.RecipeDiscoveryFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreDependencies::class])
interface RecipeDiscoveryComponent {

    fun inject(activity: RecipeDiscoveryActivity)
    fun inject(fragment: RecipeDiscoveryFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): RecipeDiscoveryComponent

    }

}