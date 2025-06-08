package com.youkydesign.favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
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
                        UiResource.Error("Sorry, something went wrong! We can't get this recipe right now.")
                }
                .collect { state: UiResource<Recipe?> ->
                    when (state) {
                        is UiResource.Error -> {
                            if (state.message == null) {
                                _recipeDetailState.value =
                                    UiResource.Error("Sorry, something went wrong! We can't get this recipe right now.")
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
                                    UiResource.Error("Sorry, something went wrong! We can't get this recipe right now.")
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