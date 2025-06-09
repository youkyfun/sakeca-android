package com.youkydesign.sakeca.feature.groceries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youkydesign.sakeca.core.di.AppScope
import com.youkydesign.sakeca.domain.groceries.GroceriesUseCase
import com.youkydesign.sakeca.domain.groceries.Grocery
import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@AppScope
@Singleton
internal class GroceriesViewModel @Inject constructor(private val useCase: GroceriesUseCase) :
    ViewModel() {
    private val _uiState: MutableStateFlow<UiResource<List<Grocery>>> =
        MutableStateFlow(UiResource.Idle())
    val uiState: StateFlow<UiResource<List<Grocery>>> = _uiState.asStateFlow()

    init {
        getAll()
    }

    fun getAll() {
        _uiState.value = UiResource.Loading()
        viewModelScope.launch {
            useCase.getAll().collect { state: UiResource<List<Grocery>> ->
                _uiState.value = state
            }
        }
    }

    fun delete(grocery: Grocery) {
        viewModelScope.launch {
            useCase.delete(grocery)
        }
    }

    fun insert(grocery: Grocery) {
        viewModelScope.launch {
            useCase.insert(grocery)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            useCase.deleteAll()
        }
    }
}