package com.youkydesign.core

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.youkydesign.core.data.local.LocalRecipeDataSource
import com.youkydesign.core.data.network.ApiResponse
import com.youkydesign.core.data.network.NetworkRecipeDataSource
import com.youkydesign.core.data.network.response.RecipeResponse
import com.youkydesign.core.domain.IRecipeRepository
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val localDataSource: LocalRecipeDataSource,
    private val networkDataSource: NetworkRecipeDataSource
) : IRecipeRepository {
    override fun getAllCachedRecipes(): Flow<UiResource<List<Recipe>>> = flow {
        localDataSource.getAllCachedRecipes().collect { recipeList ->
            if (recipeList.isEmpty()) {
                emit(UiResource.Success(emptyList()))
                return@collect
            } else {
                emit(UiResource.Success(recipeList.map {
                    DataMapper.mapEntityToDomain(it)
                }))
            }
        }
    }

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
        val date = System.currentTimeMillis()
        val newRecipe = DataMapper.mapDomainToEntity(recipe)
        localDataSource.setFavoriteRecipe(newRecipe.copy(isFavorite = isFavorite, date = date))
    }

    override fun getFavoriteRecipes(sortType: RecipeSortType): Flow<UiResource<List<Recipe>>> =
        flow {
            val config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE
            )

            Pager(
                config = config,
                pagingSourceFactory = {
                    localDataSource.getFavoriteRecipes(sortType)
                }
            ).flow.map { pagingData ->
                pagingData.map {
                    Log.d("Repository", "getFavoriteRecipes: $it")
                    DataMapper.mapEntityToDomain(it)
                }
            }
        }

    companion object {
        const val PAGE_SIZE = 5
        const val INITIAL_LOAD_SIZE = 10
    }
}