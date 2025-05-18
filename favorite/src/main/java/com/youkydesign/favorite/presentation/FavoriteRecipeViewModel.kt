package com.youkydesign.favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.RecipeUseCase
import com.youkydesign.core.domain.UiResource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteRecipeViewModel(private val recipeUseCase: RecipeUseCase) : ViewModel() {
    private val _favoriteRecipes: MutableLiveData<UiResource<List<Recipe>>> =
        MutableLiveData(UiResource.Idle())
    val favoriteRecipes: LiveData<UiResource<List<Recipe>>> = _favoriteRecipes

    init {
        getFavoriteRecipes()
    }

    private fun getFavoriteRecipes() {
        _favoriteRecipes.value = UiResource.Loading()
        viewModelScope.launch {
            recipeUseCase.getFavoriteRecipes()
                .catch {
                    _favoriteRecipes.value = UiResource.Error(it.message.toString())
                }
                .collect { state: UiResource<List<Recipe>> ->
                    when (state) {
                        is UiResource.Idle -> {}
                        is UiResource.Loading -> {
                            _favoriteRecipes.value = UiResource.Loading()
                        }

                        is UiResource.Success -> {
                            _favoriteRecipes.value = UiResource.Success(state.data!!)
                        }

                        is UiResource.Error -> {
                            _favoriteRecipes.value = UiResource.Error(state.message!!)
                        }
                    }
                }
        }
    }
}