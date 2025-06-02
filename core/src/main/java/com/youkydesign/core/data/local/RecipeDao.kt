package com.youkydesign.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getCachedRecipes(): Flow<List<RecipeEntity>>

    @RawQuery(observedEntities = [RecipeEntity::class])
    fun getFavoriteRecipes(query: SupportSQLiteQuery): PagingSource<Int, RecipeEntity>

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    fun getRecipeById(recipeId: String): Flow<RecipeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(recipe: RecipeEntity)

    @Update
    suspend fun setFavoriteRecipe(recipe: RecipeEntity)
}