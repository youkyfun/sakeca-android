package com.youkydesign.core.domain

import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    fun searchRecipes(query: String): Flow<UiResource<List<Recipe>>>
    fun getRecipe(rId: String): Flow<UiResource<Recipe?>>
}