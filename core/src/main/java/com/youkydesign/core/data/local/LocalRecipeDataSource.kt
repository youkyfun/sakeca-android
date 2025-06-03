package com.youkydesign.core.data.local

import android.util.Log
import androidx.paging.PagingSource
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.SortUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeDataSource @Inject constructor(private val recipeDao: RecipeDao) {
    fun getAllCachedRecipes(): Flow<List<RecipeEntity>> = recipeDao.getCachedRecipes()

    fun getRecipe(rId: String): Flow<RecipeEntity?> = recipeDao.getRecipeById(rId)

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.saveRecipe(recipe)
    }

    suspend fun setFavoriteRecipe(newRecipe: RecipeEntity) {
        recipeDao.setFavoriteRecipe(newRecipe)
    }

    fun getFavoriteRecipes(sortType: RecipeSortType): PagingSource<Int, RecipeEntity> {
        val query = SortUtils.getSortedQuery(sortType)
        val getFavoriteRecipes = recipeDao.getFavoriteRecipes(query)
        Log.d("LocalData", "getFavoriteRecipes: $getFavoriteRecipes")

        return getFavoriteRecipes
    }
}