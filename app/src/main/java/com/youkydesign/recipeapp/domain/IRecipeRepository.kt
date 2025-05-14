package com.youkydesign.recipeapp.domain

import com.youkydesign.recipeapp.data.Resource
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    fun searchRecipes(query: String): Flow<Resource<List<Recipe>>>
    fun getRecipe(rId: String): Flow<Resource<Recipe?>>
}