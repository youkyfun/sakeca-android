package com.youkydesign.feature.details.di

import android.content.Context
import com.youkydesign.core.di.AppScope
import com.youkydesign.core.di.CoreDependencies
import com.youkydesign.feature.details.presentation.RecipeDetailsActivity
import com.youkydesign.feature.details.presentation.RecipeDetailsFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreDependencies::class])
interface RecipeDetailsComponent {

    fun inject(activity: RecipeDetailsActivity)
    fun inject(fragment: RecipeDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): RecipeDetailsComponent

    }

}