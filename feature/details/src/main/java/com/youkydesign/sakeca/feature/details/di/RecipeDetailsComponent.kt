package com.youkydesign.sakeca.feature.details.di

import android.content.Context
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.di.CoreDependencies
import com.youkydesign.sakeca.feature.details.presentation.RecipeDetailActivity
import com.youkydesign.sakeca.feature.details.presentation.RecipeDetailsActivity
import com.youkydesign.sakeca.feature.details.presentation.RecipeDetailsFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreDependencies::class])
interface RecipeDetailsComponent {

    fun inject(activity: RecipeDetailsActivity)
    fun inject(compose: RecipeDetailActivity)
    fun inject(fragment: RecipeDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): RecipeDetailsComponent
    }
}