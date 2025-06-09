package com.youkydesign.sakeca.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youkydesign.sakeca.data.groceries.GroceryDao
import com.youkydesign.sakeca.data.groceries.GroceryEntity

@Database(entities = [RecipeEntity::class, GroceryEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun groceriesDao(): GroceryDao
}