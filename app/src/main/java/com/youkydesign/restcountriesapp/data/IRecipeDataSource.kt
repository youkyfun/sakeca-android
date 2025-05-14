package com.youkydesign.restcountriesapp.data

import com.youkydesign.restcountriesapp.data.network.ApiResponse
import com.youkydesign.restcountriesapp.domain.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipeDataSource {
    fun getRecipes(query: String): Flow<ApiResponse<List<Recipe>>>
    fun getRecipe(rId: String): Flow<ApiResponse<Recipe?>>
}