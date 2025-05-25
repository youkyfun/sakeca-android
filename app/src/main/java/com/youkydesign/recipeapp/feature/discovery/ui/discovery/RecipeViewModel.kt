package com.youkydesign.recipeapp.feature.discovery.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _searchResult: MutableLiveData<UiResource<List<Recipe>>> =
        MutableLiveData(UiResource.Idle())
    val searchResult: LiveData<UiResource<List<Recipe>>> = _searchResult

    init {
        getAllCachedRecipes()
        searchRecipes("chicken")
    }

    private fun getAllCachedRecipes() {
        _searchResult.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.getAllCachedRecipes().collect { state: UiResource<List<Recipe>> ->
                when (state) {
                    is UiResource.Error -> _searchResult.value = state
                    is UiResource.Idle -> _searchResult.value = state
                    is UiResource.Loading -> _searchResult.value = state
                    is UiResource.Success -> {
                        if (state.data?.isEmpty() == true) {
                            searchRecipes("chicken")
                        } else {
                            _searchResult.value = state
                        }
                    }
                }
            }
        }
    }

    fun searchRecipes(query: String) {
        _searchResult.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.searchRecipes(query).collect { state: UiResource<List<Recipe>> ->
                _searchResult.value = state
            }
        }
    }

}