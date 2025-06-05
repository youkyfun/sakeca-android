package com.youkydesign.recipeapp.feature.discovery.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
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