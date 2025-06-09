package com.youkydesign.sakeca.feature.discovery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import  com.youkydesign.sakeca.core.domain.Recipe
import  com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class RecipeSearchViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _searchResult: MutableStateFlow<UiResource<List<Recipe>>> =
        MutableStateFlow(UiResource.Idle())
    val searchResult: StateFlow<UiResource<List<Recipe>>> = _searchResult.asStateFlow()

    init {
        searchRecipes("chicken")
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