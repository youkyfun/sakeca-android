package com.youkydesign.sakeca.feature.groceries.di

import android.content.Context
import com.youkydesign.sakeca.core.di.CoreDependencies
import com.youkydesign.sakeca.core.di.FeatureScope
import com.youkydesign.sakeca.feature.groceries.presentation.GroceriesRootFragment
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(dependencies = [CoreDependencies::class])
interface GroceriesComponent {

    fun inject(fragment: GroceriesRootFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): GroceriesComponent

    }
}