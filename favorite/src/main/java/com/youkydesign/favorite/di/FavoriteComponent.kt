package com.youkydesign.favorite.di

import android.content.Context
import com.youkydesign.core.di.RecipeModuleDependencies
import com.youkydesign.favorite.FavoriteActivity
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [RecipeModuleDependencies::class])
interface FavoriteComponent {

    fun inject(activity: FavoriteActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: RecipeModuleDependencies
        ): FavoriteComponent

    }
}