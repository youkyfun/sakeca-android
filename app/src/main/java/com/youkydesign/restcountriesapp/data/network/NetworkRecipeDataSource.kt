package com.youkydesign.restcountriesapp.data.network

import android.util.Log
import com.youkydesign.restcountriesapp.data.network.response.RecipeResponse
import com.youkydesign.restcountriesapp.data.network.response.RecipesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRecipeDataSource @Inject constructor(private val apiService: ApiService) {
    fun getRecipes(query: String): Flow<ApiResponse<List<RecipesItem>>> = flow {
        try {
            Log.d("RemoteDataSource", query)
            val response = apiService.getRecipes(query)
            val dataArray = response.recipes
            if (dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(response.recipes))
                Log.e("RemoteDataSource", response.recipes.toString())
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    fun getRecipe(rId: String): Flow<ApiResponse<RecipeResponse>> = flow {
        try {
            val response = apiService.getRecipe(rId)
            val recipe = response?.recipeResponse
            if (recipe != null) {
                emit(ApiResponse.Success(response.recipeResponse))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}