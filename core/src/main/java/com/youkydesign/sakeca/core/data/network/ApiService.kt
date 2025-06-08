package com.youkydesign.sakeca.core.data.network

import com.youkydesign.sakeca.core.data.network.response.RecipeDetailsResponse
import com.youkydesign.sakeca.core.data.network.response.RecipeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun searchRecipes(@Query("q") q: String = "pizza"): RecipeListResponse

    @GET("get")
    suspend fun getRecipe(@Query("rId") rId: String): RecipeDetailsResponse?
}