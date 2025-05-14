package com.youkydesign.restcountriesapp.data

import android.util.Log
import com.youkydesign.restcountriesapp.data.local.LocalRecipeDataSource
import com.youkydesign.restcountriesapp.data.network.ApiResponse
import com.youkydesign.restcountriesapp.data.network.NetworkRecipeDataSource
import com.youkydesign.restcountriesapp.data.network.response.RecipeResponse
import com.youkydesign.restcountriesapp.data.network.response.RecipesItem
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
    override fun getRecipes(query: String): Flow<Resource<List<Recipe>>> =
        object : NetworkBoundResource<List<Recipe>, List<RecipesItem>>() {
            override fun loadFromDB(): Flow<List<Recipe>> {
                return localDataSource.getRecipes().map {
                    it.map { recipeEntity ->
                        DataMapper.mapEntityToDomain(recipeEntity)
                    }
                }
            }

            override fun shouldFetch(data: List<Recipe>?): Boolean = data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<RecipesItem>>> {
                Log.d("Repos", query)
                return networkDataSource.getRecipes(query)
            }

            override suspend fun saveCallResult(data: List<RecipesItem>) {
                val recipeList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertRecipes(recipeList)
            }

        }.asFlow()

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