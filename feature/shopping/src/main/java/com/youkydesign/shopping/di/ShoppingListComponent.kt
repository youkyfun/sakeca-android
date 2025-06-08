package com.youkydesign.shopping.di

import android.content.Context
import com.youkydesign.core.di.AppScope
import com.youkydesign.core.di.CoreDependencies
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreDependencies::class])
interface ShoppingListComponent {

    fun inject(fragment: ShoppingListComponent)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): ShoppingListComponent

    }
}