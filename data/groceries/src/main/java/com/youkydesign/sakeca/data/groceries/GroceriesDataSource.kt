package com.youkydesign.sakeca.data.groceries

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceriesDataSource @Inject constructor(private val groceryDao: GroceryDao) {
    fun getAll(): List<GroceryEntity> = groceryDao.getAll()
    fun insert(grocery: GroceryEntity) {
        Log.d("AddIngredientToShoppingBag", "Inserting grocery: $grocery")
        groceryDao.insert(grocery)
    }

    fun update(grocery: GroceryEntity) = groceryDao.update(grocery)
    fun delete(grocery: GroceryEntity) = groceryDao.delete(grocery)
    fun deleteAll() = groceryDao.deleteAll()
}