package com.youkydesign.favorite.di

import android.content.Context
import com.youkydesign.core.di.FavoriteModuleDependencies
import com.youkydesign.favorite.ui.main.FavoriteActivity
import com.youkydesign.favorite.ui.main.FavoriteMainFragment
import com.youkydesign.favorite.ui.details.FavoriteDetailsFragment
import com.youkydesign.recipeapp.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [FavoriteModuleDependencies::class])
interface FavoriteComponent {

    fun inject(activity: FavoriteActivity)
    fun inject(mainFragment: FavoriteMainFragment)
    fun inject(detailsFragment: FavoriteDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: FavoriteModuleDependencies
        ): FavoriteComponent

    }

}