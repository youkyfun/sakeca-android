package com.youkydesign.recipeapp.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.recipeapp.data.Resource
import com.youkydesign.recipeapp.domain.Recipe
import com.youkydesign.recipeapp.domain.RecipeUseCase
import kotlinx.coroutines.flow.catch
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
            recipeUseCase.getRecipe(rId)
                .catch { exception ->
                    _recipeDetailState.value =
                        Resource.Error("Sorry, something went wrong! We can't get this recipe right now.")
                }
                .collect { state: Resource<Recipe?> ->
                    if (state is Resource.Success) {
                        _recipeDetailState.value = Resource.Success(state.data!!)
                    } else if (state is Resource.Error) {
                        _recipeDetailState.value = Resource.Error(state.message!!)
                    }
                }
        }
    }
}