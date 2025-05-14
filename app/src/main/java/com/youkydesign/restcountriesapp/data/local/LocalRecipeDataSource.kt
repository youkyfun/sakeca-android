package com.youkydesign.restcountriesapp.data.local

import com.youkydesign.restcountriesapp.domain.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeDataSource @Inject constructor(private val recipeDao: RecipeDao) {
    fun getRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    fun getRecipe(rId: String): Flow<RecipeEntity?> = recipeDao.getRecipeById(rId)

    suspend fun insertRecipes(recipes: List<RecipeEntity>) {
        recipeDao.insertRecipes(recipes)
    }

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }
}