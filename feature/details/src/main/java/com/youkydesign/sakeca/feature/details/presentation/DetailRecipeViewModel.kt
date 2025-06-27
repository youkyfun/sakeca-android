package com.youkydesign.sakeca.feature.details.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.sakeca.core.domain.Recipe
import com.youkydesign.sakeca.core.domain.RecipeUseCase
import com.youkydesign.sakeca.domain.groceries.GroceriesUseCase
import com.youkydesign.sakeca.domain.groceries.Grocery
import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal class DetailRecipeViewModel(
    private val recipeUseCase: RecipeUseCase,
    private val groceryUseCase: GroceriesUseCase
) : ViewModel() {
    private val _ingredientsToSave: MutableStateFlow<List<Grocery>> = MutableStateFlow(emptyList())
    val ingredientsToSave: StateFlow<List<Grocery>> = _ingredientsToSave.asStateFlow()

    private val _recipeDetailState: MutableStateFlow<UiResource<Recipe>> =
        MutableStateFlow(UiResource.Loading())
    val recipeDetailState: StateFlow<UiResource<Recipe>> = _recipeDetailState.asStateFlow()

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

    fun setIngredientsToSave(ingredient: String) {
        Log.d("DetailRecipeViewModel", "Ingredient to save: $ingredient")
        val newGrocery = Grocery(name = ingredient)
        val currentList = _ingredientsToSave.value.toMutableList()
        currentList.add(newGrocery)
        _ingredientsToSave.value = currentList.toList()
    }

    fun removeIngredientFromList(ingredient: String) {
        viewModelScope.launch {
            val currentList = _ingredientsToSave.value.toMutableList()
            currentList.removeIf {
                Log.d("DetailRecipeViewModel", "Ingredient to remove: $ingredient")
                it.name == ingredient
            }
            _ingredientsToSave.value = currentList.toList()
        }
    }

    fun addIngredientToShoppingBag() {
        if (ingredientsToSave.value.isEmpty()) {
            return
        }
        viewModelScope.launch {
            if (ingredientsToSave.value.isNotEmpty()) {
                ingredientsToSave.value.forEach {
                    groceryUseCase.insert(it)
                }
            }
        }
    }
}