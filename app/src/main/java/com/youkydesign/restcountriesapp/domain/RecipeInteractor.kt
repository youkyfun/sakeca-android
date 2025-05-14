package com.youkydesign.restcountriesapp.domain

import com.youkydesign.restcountriesapp.data.Resource
import dagger.Module
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Module
class RecipeInteractor @Inject constructor(private val recipeRepository: IRecipeRepository): RecipeUseCase {
    override fun searchRecipes(query: String): Flow<Resource<List<Recipe>>> = recipeRepository.searchRecipes(query)

    override fun getRecipe(rId: String): Flow<Resource<Recipe>> = recipeRepository.getRecipe(rId)

}