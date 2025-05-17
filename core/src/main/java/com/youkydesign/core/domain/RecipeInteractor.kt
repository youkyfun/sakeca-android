package com.youkydesign.core.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeInteractor @Inject constructor(private val recipeRepository: IRecipeRepository) :
    RecipeUseCase {
    override fun searchRecipes(query: String): Flow<UiResource<List<Recipe>>> =
        recipeRepository.searchRecipes(query)

    override fun getRecipe(rId: String): Flow<UiResource<Recipe?>> = recipeRepository.getRecipe(rId)
    override suspend fun setFavoriteRecipe(
        recipe: Recipe,
        isFavorite: Boolean
    ) {
        recipeRepository.setFavoriteRecipe(recipe, isFavorite)
    }

    override fun getFavoriteRecipes(): Flow<UiResource<List<Recipe>>> =
        recipeRepository.getFavoriteRecipes()

}