package com.youkydesign.sakeca.feature.groceries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youkydesign.sakeca.core.di.FeatureScope
import com.youkydesign.sakeca.domain.groceries.GroceriesUseCase
import javax.inject.Inject

@FeatureScope
class GroceriesViewModelFactory @Inject constructor(private val useCase: GroceriesUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            GroceriesViewModel::class.java -> GroceriesViewModel(useCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}