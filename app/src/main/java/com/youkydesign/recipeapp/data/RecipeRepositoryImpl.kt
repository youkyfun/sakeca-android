package com.youkydesign.recipeapp.data

import com.youkydesign.recipeapp.data.local.LocalRecipeDataSource
import com.youkydesign.recipeapp.data.network.ApiResponse
import com.youkydesign.recipeapp.data.network.NetworkRecipeDataSource
import com.youkydesign.recipeapp.data.network.response.RecipeResponse
import com.youkydesign.recipeapp.domain.IRecipeRepository
import com.youkydesign.recipeapp.domain.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalRecipeDataSource,
    private val networkDataSource: NetworkRecipeDataSource
) : IRecipeRepository {
    override fun searchRecipes(query: String): Flow<Resource<List<Recipe>>> =
        networkDataSource.searchRecipes(query).map { response ->
            when (response) {
                is ApiResponse.Success -> Resource.Success(data = response.data.map {
                    DataMapper.mapListItemResponseToDomain(it)
                })

                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        }

    override fun getRecipe(rId: String): Flow<Resource<Recipe?>> =
        object : NetworkBoundResource<Recipe?, RecipeResponse>() {
            override fun loadFromDB(): Flow<Recipe?> {
                return localDataSource.getRecipe(rId).map {
                    if (it == null) {
                        null
                    } else {
                        DataMapper.mapEntityToDomain(it)
                    }
                }
            }

            override fun shouldFetch(data: Recipe?): Boolean = data == null

            override suspend fun createCall(): Flow<ApiResponse<RecipeResponse>> {
                val response = networkDataSource.getRecipe(rId)
                return response
            }

            override suspend fun saveCallResult(data: RecipeResponse) {
                val recipe = DataMapper.mapResponseToEntity(data)
                localDataSource.insertRecipe(recipe)
            }
        }.asFlow()
}