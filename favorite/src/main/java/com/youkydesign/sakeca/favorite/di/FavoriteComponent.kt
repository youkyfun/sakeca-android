package com.youkydesign.sakeca.favorite.di

import android.content.Context
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.core.di.CoreDependencies
import com.youkydesign.sakeca.favorite.presentation.FavoriteActivity
import com.youkydesign.sakeca.favorite.presentation.FavoriteMainFragment
import com.youkydesign.sakeca.favorite.presentation.RecipeDetailsFragment
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