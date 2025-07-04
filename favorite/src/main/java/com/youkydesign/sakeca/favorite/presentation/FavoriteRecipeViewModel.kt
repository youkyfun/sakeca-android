package com.youkydesign.sakeca.favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.youkydesign.sakeca.core.RecipeSortType
import com.youkydesign.sakeca.core.domain.Recipe
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.core.domain.UiResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import com.youkydesign.sakeca.designsystem.R as designSystem

internal class FavoriteRecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _favoriteRecipes: MutableStateFlow<PagingData<Recipe>> =
        MutableStateFlow(PagingData.empty())
    val favoriteRecipes: StateFlow<PagingData<Recipe>> = _favoriteRecipes.asStateFlow()

    private val _recipeDetailState: MutableLiveData<UiResource<Recipe>> =
        MutableLiveData(UiResource.Loading())
    val recipeDetailState: LiveData<UiResource<Recipe>> = _recipeDetailState

    init {
        this@FavoriteRecipeViewModel.getFavoriteRecipes()
    }

    fun getRecipe(rId: String) {
        _recipeDetailState.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.getRecipe(rId)
                .catch {
                    _recipeDetailState.value =
                        UiResource.Error(designSystem.string.no_recipe_found.toString())
                }
                .collect { state: UiResource<Recipe?> ->
                    when (state) {
                        is UiResource.Error -> {
                            if (state.message == null) {
                                _recipeDetailState.value =
                                    UiResource.Error(designSystem.string.no_recipe_found.toString())
                                return@collect
                            } else {
                                _recipeDetailState.value = UiResource.Error(state.message!!)
                            }
                        }

                        is UiResource.Idle -> {}
                        is UiResource.Loading -> {
                            _recipeDetailState.value = UiResource.Loading()
                        }

                        is UiResource.Success<*> -> {
                            if (state.data == null) {
                                _recipeDetailState.value =
                                    UiResource.Error(designSystem.string.no_recipe_found.toString())
                                return@collect
                            } else {
                                _recipeDetailState.value = UiResource.Success(state.data!!)
                            }
                        }
                    }
                }
        }
    }

    fun setFavoriteRecipe(recipe: Recipe?, isFavorite: Boolean) {
        viewModelScope.launch {
            if (recipe == null) {
                cancel()
                return@launch
            }
            recipeUseCase.setFavoriteRecipe(recipe, isFavorite)
        }
    }

    fun getFavoriteRecipes(sortType: RecipeSortType = RecipeSortType.BY_DATE_DESC) {
        viewModelScope.launch {
            recipeUseCase.getFavoriteRecipes(sortType)
                .catch {
                    _favoriteRecipes.value = PagingData.empty()
                }
                .collect {
                    _favoriteRecipes.value = it
                }
        }
    }
}