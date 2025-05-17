package com.youkydesign.core

import com.youkydesign.core.data.network.ApiResponse
import com.youkydesign.core.domain.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipeDataSource {
    fun getRecipes(query: String): Flow<ApiResponse<List<Recipe>>>
    fun getRecipe(rId: String): Flow<ApiResponse<Recipe?>>
}