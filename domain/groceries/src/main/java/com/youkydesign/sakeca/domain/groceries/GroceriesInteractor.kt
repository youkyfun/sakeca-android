package com.youkydesign.sakeca.domain.groceries

import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceriesInteractor @Inject constructor(private val repository: IGroceriesRepository) :
    GroceriesUseCase {
    override fun getAll(): Flow<UiResource<List<Grocery>>> = repository.getAll()

    override fun insert(grocery: Grocery) = repository.insert(grocery)

    override fun delete(grocery: Grocery) = repository.delete(grocery)

    override fun deleteAll() = repository.deleteAll()
}