package com.youkydesign.core.di

import android.content.Context

object CoreInjectHelper {
    fun provideCoreComponent(applicationContext: Context): CoreComponent {
        return if (applicationContext is CoreComponentProvider) {
            (applicationContext as CoreComponentProvider).provideCoreComponent()
        } else {
            throw IllegalStateException("Your application context is not an instance of CoreComponentProvider")
        }
    }
}