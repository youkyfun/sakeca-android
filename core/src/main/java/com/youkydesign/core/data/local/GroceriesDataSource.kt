package com.youkydesign.core.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceriesDataSource @Inject constructor(private val groceryDao: GroceryDao) {
    fun getAll(): List<GroceryEntity> = groceryDao.getAll()
    fun insert(grocery: GroceryEntity) = groceryDao.insert(grocery)
    fun update(grocery: GroceryEntity) = groceryDao.update(grocery)
    fun delete(grocery: GroceryEntity) = groceryDao.delete(grocery)
    fun deleteAll() = groceryDao.deleteAll()
}