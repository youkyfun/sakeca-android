package com.youkydesign.favorite.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.youkydesign.core.RecipeSortType
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteRecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _filter = MutableLiveData<RecipeSortType>()

    val favoriteRecipes: LiveData<PagingData<Recipe>> = _filter.switchMap {
        recipeUseCase.getFavoriteRecipes(it).cachedIn(viewModelScope).asLiveData()
    }
    private val _recipeDetailState: MutableLiveData<UiResource<Recipe>> =
        MutableLiveData(UiResource.Loading())

    val recipeDetailState: LiveData<UiResource<Recipe>> = _recipeDetailState

    init {
        _filter.value = RecipeSortType.BY_DATE_ASC
    }

    fun filter(filterType: RecipeSortType) {
        _filter.value = filterType
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