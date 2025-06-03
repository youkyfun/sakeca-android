package com.youkydesign.favorite.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteRecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _favoriteRecipes: MutableLiveData<UiResource<List<Recipe>>> =
        MutableLiveData(UiResource.Idle())
    val favoriteRecipes: LiveData<UiResource<List<Recipe>>> = _favoriteRecipes
    private val _recipeDetailState: MutableLiveData<UiResource<Recipe>> =
        MutableLiveData(UiResource.Loading())

    val recipeDetailState: LiveData<UiResource<Recipe>> = _recipeDetailState


    init {
        getFavoriteRecipes(RecipeSortType.BY_DATE)
    }

    fun getFavoriteRecipes(sortType: RecipeSortType) {
        _favoriteRecipes.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.getFavoriteRecipes(sortType)
                .catch {
                    _favoriteRecipes.value =
                        UiResource.Error(it.message ?: "An unknown error occurred")
                }
                .collect { state ->
                    Log.d("VM", "getFavoriteRecipes: $state")

                    when (state) {
                        is UiResource.Idle -> {}
                        is UiResource.Loading -> {
                            _favoriteRecipes.value = UiResource.Loading()
                        }

                        is UiResource.Success -> {
                            _favoriteRecipes.value = UiResource.Success(state.data ?: emptyList())
                        }

                        is UiResource.Error -> {
                            _favoriteRecipes.value =
                                UiResource.Error(state.message ?: "Something went wrong")
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
            getFavoriteRecipes(RecipeSortType.BY_DATE)
        }
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
}