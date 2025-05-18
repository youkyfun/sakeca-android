package com.youkydesign.core

import com.youkydesign.core.data.local.LocalRecipeDataSource
import com.youkydesign.core.data.network.ApiResponse
import com.youkydesign.core.data.network.NetworkRecipeDataSource
import com.youkydesign.core.data.network.response.RecipeResponse
import com.youkydesign.core.domain.IRecipeRepository
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalRecipeDataSource,
    private val networkDataSource: NetworkRecipeDataSource
) : IRecipeRepository {
    override fun searchRecipes(query: String): Flow<UiResource<List<Recipe>>> =
        networkDataSource.searchRecipes(query).map { response ->
            when (response) {
                is ApiResponse.Success -> UiResource.Success(data = response.data.map {
                    DataMapper.mapListItemResponseToDomain(it)
                })

                is ApiResponse.Empty -> UiResource.Success(emptyList())
                is ApiResponse.Error -> UiResource.Error(response.errorMessage)
            }
        }

    override fun getRecipe(rId: String): Flow<UiResource<Recipe?>> =
        object : NetworkBoundResource<Recipe?, RecipeResponse>() {
            override fun loadFromDB(): Flow<Recipe?> {
                return localDataSource.getRecipe(rId).map {
                    if (it != null) {
                        DataMapper.mapEntityToDomain(it)
                    } else {
                        null
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

    override suspend fun setFavoriteRecipe(
        recipe: Recipe,
        isFavorite: Boolean
    ) {
        val newRecipe = DataMapper.mapDomainToEntity(recipe)
        localDataSource.setFavoriteRecipe(newRecipe.copy(isFavorite = isFavorite))
    }

    override fun getFavoriteRecipes(): Flow<UiResource<List<Recipe>>> = flow {
        val favoriteRecipes = localDataSource.getFavoriteRecipes()
        favoriteRecipes.collect { recipeList ->
            if (recipeList.isEmpty()) {
                emit(UiResource.Success(emptyList()))
                return@collect
            } else {
                emit(UiResource.Success(recipeList.map {
                    DataMapper.mapEntityToDomain(it)
                }))
            }
        }
    }.flowOn(Dispatchers.IO)
}