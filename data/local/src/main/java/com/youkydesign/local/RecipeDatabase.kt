package com.youkydesign.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youkydesign.recipeapp.data.StringListConverter

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}