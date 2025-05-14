package com.youkydesign.restcountriesapp.domain

import com.youkydesign.restcountriesapp.data.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeUseCase {
    fun searchRecipes(query: String): Flow<Resource<List<Recipe>>>
    fun getRecipe(rId: String): Flow<Resource<Recipe>>
}