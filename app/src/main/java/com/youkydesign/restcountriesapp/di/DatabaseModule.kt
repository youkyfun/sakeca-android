package com.youkydesign.restcountriesapp.di

import android.content.Context
import androidx.room.Room
import com.youkydesign.restcountriesapp.data.local.RecipeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): RecipeDatabase {
        return Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
            .build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase) = database.recipeDao()

}