package com.youkydesign.recipeapp.data

import com.youkydesign.recipeapp.data.network.ApiResponse
import com.youkydesign.recipeapp.domain.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipeDataSource {
    fun getRecipes(query: String): Flow<ApiResponse<List<Recipe>>>
    fun getRecipe(rId: String): Flow<ApiResponse<Recipe?>>
}