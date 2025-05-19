package com.youkydesign.core.di

import android.content.Context
import androidx.room.Room
import com.youkydesign.core.data.local.RecipeDatabase
import dagger.Module
import dagger.Provides
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): RecipeDatabase {
        val passPhrase: ByteArray = SQLiteDatabase.getBytes("recipe".toCharArray())
        val encryptFactory = SupportFactory(passPhrase)
        return Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
            .fallbackToDestructiveMigration(false)
            .openHelperFactory(encryptFactory)
            .build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase) = database.recipeDao()

}