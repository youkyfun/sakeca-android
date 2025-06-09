package com.youkydesign.sakeca.domain.groceries

import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.flow.Flow

interface IGroceriesRepository {
    fun getAll(): Flow<UiResource<List<Grocery>>>
    fun insert(grocery: Grocery)
    fun update(grocery: Grocery)
    fun delete(grocery: Grocery)
    fun deleteAll()
}