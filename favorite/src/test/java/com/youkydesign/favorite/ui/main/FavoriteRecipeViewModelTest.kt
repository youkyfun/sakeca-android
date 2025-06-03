package com.youkydesign.favorite.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FavoriteRecipeViewModelTest {
    private lateinit var recipeUseCase: RecipeUseCase
    private lateinit var viewModel: FavoriteRecipeViewModel

    val recipe = Recipe(
        publisher = "publisher 1",
        title = "Recipe 1",
        sourceUrl = "sourceUrl 1",
        recipeId = "1123",
        imageUrl = "imageUrl 1",
        socialRank = 99.8,
        publisherUrl = "publisherUrl 1",
        ingredients = listOf("ing 1", "ing 2", "ing 3"),
        isFavorite = false
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        recipeUseCase = mock()
        viewModel = FavoriteRecipeViewModel(recipeUseCase)
    }

    @Test
    fun `setFavoriteRecipe  Mark a new recipe as favorite`() = runTest {
        // Given
        val recipeToFavorite = recipe.copy()
        val shouldBeFavorite = true

        // When
        viewModel.setFavoriteRecipe(recipeToFavorite, shouldBeFavorite)

        // Then
        verify(recipeUseCase).setFavoriteRecipe(recipeToFavorite, shouldBeFavorite)
    }

    @Test
    fun `setFavoriteRecipe  Mark an existing recipe as not favorite`() = runTest {

        // Given
        val recipeToFavorite = recipe.copy(isFavorite = true)
        val shouldNotBeFavorite = false

        // When
        viewModel.setFavoriteRecipe(recipeToFavorite, shouldNotBeFavorite)

        // Then
        verify(recipeUseCase).setFavoriteRecipe(recipeToFavorite, shouldNotBeFavorite)
    }

    @Test
    fun `setFavoriteRecipe  Mark an existing recipe as favorite`() = runTest {
        // Given
        val recipeToFavorite = recipe.copy()
        val shouldBeFavorite = true
        // When
        viewModel.setFavoriteRecipe(recipeToFavorite, shouldBeFavorite)

        // Then
        verify(recipeUseCase).setFavoriteRecipe(recipeToFavorite, shouldBeFavorite)
    }

    @Test
    fun `getFavoriteRecipes  returns favorite recipes`() = runTest {
        val sortType = RecipeSortType.BY_DATE
        // Given
        val favoriteRecipes = listOf(recipe)
        val expectedState = UiResource.Success(favoriteRecipes)
        whenever(recipeUseCase.getFavoriteRecipes(sortType)).thenReturn(flowOf(expectedState))
        // When
        viewModel.getFavoriteRecipes(sortType)
        // Then
        val actualState = viewModel.favoriteRecipes.value
        assertEquals(expectedState.data, actualState?.data)
    }

    @Test
    fun `getFavoriteRecipe  returns single favorite recipe`() = runTest {
        // Given
        val expectedState = UiResource.Success(recipe)
        whenever(recipeUseCase.getRecipe(recipe.recipeId)).thenReturn(flowOf(expectedState as UiResource<Recipe?>))
        // When
        viewModel.getRecipe(recipe.recipeId)
        // Then
        val actualState = viewModel.recipeDetailState.value
        assertEquals(expectedState.data, actualState?.data)
    }


}

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}