package com.youkydesign.recipeapp.feature.discovery.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _searchResult: MutableLiveData<UiResource<List<Recipe>>> =
        MutableLiveData(UiResource.Idle())
    val searchResult: LiveData<UiResource<List<Recipe>>> = _searchResult

    private val _recipeDetailState: MutableLiveData<UiResource<Recipe>> =
        MutableLiveData(UiResource.Loading())
    val recipeDetailState: LiveData<UiResource<Recipe>> = _recipeDetailState

    init {
        searchRecipes("pizza")
    }

    fun searchRecipes(query: String) {
        _searchResult.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.searchRecipes(query).collect { state: UiResource<List<Recipe>> ->
                _searchResult.value = state
            }
        }
    }

    fun getRecipe(rId: String) {
        _recipeDetailState.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.getRecipe(rId)
                .catch { exception ->
                    _recipeDetailState.value =
                        UiResource.Error("Sorry, something went wrong! We can't get this recipe right now.")
                }
                .collect { state: UiResource<Recipe?> ->
                    if (state is UiResource.Success) {
                        _recipeDetailState.value = UiResource.Success(state.data!!)
                    } else if (state is UiResource.Error) {
                        _recipeDetailState.value = UiResource.Error(state.message!!)
                    }
                }
        }
    }
}