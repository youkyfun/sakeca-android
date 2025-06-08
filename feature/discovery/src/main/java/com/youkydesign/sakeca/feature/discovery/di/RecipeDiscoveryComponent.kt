package com.youkydesign.sakeca.feature.discovery.di

import android.content.Context
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.di.CoreDependencies
import com.youkydesign.sakeca.feature.discovery.presentation.RecipeDiscoveryActivity
import com.youkydesign.sakeca.feature.discovery.presentation.RecipeDiscoveryFragment
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