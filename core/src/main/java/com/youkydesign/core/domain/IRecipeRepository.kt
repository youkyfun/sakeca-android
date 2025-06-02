package com.youkydesign.core.domain

import com.youkydesign.core.RecipeSortType
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    fun getAllCachedRecipes(): Flow<UiResource<List<Recipe>>>
    fun searchRecipes(query: String): Flow<UiResource<List<Recipe>>>
    fun getRecipe(rId: String): Flow<UiResource<Recipe?>>
    suspend fun setFavoriteRecipe(recipe: Recipe, isFavorite: Boolean)
    fun getFavoriteRecipes(sortType: RecipeSortType): Flow<UiResource<List<Recipe>>>

}