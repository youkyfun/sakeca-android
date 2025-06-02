package com.youkydesign.core.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.SortUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeDataSource @Inject constructor(private val recipeDao: RecipeDao) {
    fun getAllCachedRecipes(): Flow<List<RecipeEntity>> = recipeDao.getCachedRecipes()

    fun getRecipe(rId: String): Flow<RecipeEntity?> = recipeDao.getRecipeById(rId)

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.saveRecipe(recipe)
    }

    suspend fun setFavoriteRecipe(newRecipe: RecipeEntity) {
        recipeDao.setFavoriteRecipe(newRecipe)
    }

    fun getFavoriteRecipes(sortType: RecipeSortType): Flow<PagingData<RecipeEntity>> {
        val query = SortUtils.geSortedQuery(sortType)

        val config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_SIZE
        )

        val pager = Pager(
            config = config,
            pagingSourceFactory = { recipeDao.getFavoriteRecipes(query) }
        ).flow

        return pager
    }

    companion object {
        const val PAGE_SIZE = 5
        const val INITIAL_LOAD_SIZE = 10
    }
}