package com.youkydesign.restcountriesapp.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.restcountriesapp.data.Resource
import com.youkydesign.restcountriesapp.domain.Recipe
import com.youkydesign.restcountriesapp.domain.RecipeUseCase
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _searchResult: MutableLiveData<Resource<List<Recipe>>> =
        MutableLiveData(Resource.Idle())
    val searchResult: LiveData<Resource<List<Recipe>>> = _searchResult

    private val _recipeDetailState: MutableLiveData<Resource<Recipe>> =
        MutableLiveData(Resource.Loading())
    val recipeDetailState: LiveData<Resource<Recipe>> = _recipeDetailState

    init {
        searchRecipes("pizza")
    }

    fun searchRecipes(query: String) {
        _searchResult.value = Resource.Loading()
        viewModelScope.launch {
            recipeUseCase.searchRecipes(query).collect { state: Resource<List<Recipe>> ->
                _searchResult.value = state
            }
        }
    }

    fun getRecipe(rId: String) {
        _recipeDetailState.value = Resource.Loading()
        viewModelScope.launch {
            recipeUseCase.getRecipe(rId).collect { state: Resource<Recipe> ->
                _recipeDetailState.value = state
            }
        }
    }
}