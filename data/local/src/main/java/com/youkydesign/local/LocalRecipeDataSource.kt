package com.youkydesign.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeDataSource @Inject constructor(private val recipeDao: RecipeDao) {
    fun getRecipe(rId: String): Flow<RecipeEntity?> = recipeDao.getRecipeById(rId)

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }
}