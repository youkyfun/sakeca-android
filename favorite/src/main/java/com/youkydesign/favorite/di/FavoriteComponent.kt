package com.youkydesign.favorite.di

import android.content.Context
import com.youkydesign.core.di.AppScope
import com.youkydesign.core.di.CoreDependencies
import com.youkydesign.favorite.presentation.FavoriteActivity
import com.youkydesign.favorite.presentation.FavoriteMainFragment
import com.youkydesign.favorite.presentation.RecipeDetailsFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(dependencies = [CoreDependencies::class])
interface FavoriteComponent {

    fun inject(activity: FavoriteActivity)
    fun inject(mainFragment: FavoriteMainFragment)
    fun inject(detailsFragment: RecipeDetailsFragment)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependencies: CoreDependencies
        ): FavoriteComponent

    }

}