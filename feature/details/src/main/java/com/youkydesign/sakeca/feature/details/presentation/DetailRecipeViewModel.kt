package com.youkydesign.sakeca.feature.details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import  com.youkydesign.sakeca.core.domain.Recipe
import  com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal class DetailRecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _recipeDetailState: MutableLiveData<UiResource<Recipe>> =
        MutableLiveData(UiResource.Loading())
    val recipeDetailState: LiveData<UiResource<Recipe>> = _recipeDetailState

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
}