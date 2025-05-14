package com.youkydesign.restcountriesapp.data

import com.youkydesign.restcountriesapp.data.local.LocalRecipeDataSource
import com.youkydesign.restcountriesapp.data.network.ApiResponse
import com.youkydesign.restcountriesapp.data.network.NetworkRecipeDataSource
import com.youkydesign.restcountriesapp.data.network.response.RecipeResponse
import com.youkydesign.restcountriesapp.domain.IRecipeRepository
import com.youkydesign.restcountriesapp.domain.Recipe
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
                    DataMapper.mapResponseToDomain(it)
                })

                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }

        }
    override fun getRecipe(rId: String): Flow<Resource<Recipe>> =
        object : NetworkBoundResource<Recipe, RecipeResponse>() {
            override fun loadFromDB(): Flow<Recipe> {
                return localDataSource.getRecipe(rId).map {
                    (if (it == null) {
                        null
                    } else {
                        DataMapper.mapEntityToDomain(it)
                    }) as Recipe

                }
            }

            override fun shouldFetch(data: Recipe?): Boolean = data == null

            override suspend fun createCall(): Flow<ApiResponse<RecipeResponse>> =
                networkDataSource.getRecipe(rId)

            override suspend fun saveCallResult(data: RecipeResponse) {
                val recipe = DataMapper.mapResponseToEntity(data)
                localDataSource.insertRecipe(recipe)
            }
        }.asFlow()
}