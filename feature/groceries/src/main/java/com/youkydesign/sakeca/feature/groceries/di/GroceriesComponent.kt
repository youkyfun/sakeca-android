package com.youkydesign.sakeca.feature.groceries.di

import android.content.Context
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.di.CoreDependencies
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreDependencies::class])
interface GroceriesComponent {

    fun inject(fragment: GroceriesComponent)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): GroceriesComponent

    }
}