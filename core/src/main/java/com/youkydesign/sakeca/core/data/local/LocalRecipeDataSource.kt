package com.youkydesign.sakeca.core.data.local

import androidx.paging.PagingSource
import com.youkydesign.sakeca.core.RecipeSortType
import com.youkydesign.sakeca.core.SortUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeDataSource @Inject constructor(private val recipeDao: RecipeDao) {
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

        return getFavoriteRecipes
    }
}