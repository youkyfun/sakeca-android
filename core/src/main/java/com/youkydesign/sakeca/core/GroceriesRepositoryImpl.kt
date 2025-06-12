package com.youkydesign.sakeca.core

import android.util.Log
import com.youkydesign.sakeca.data.groceries.GroceriesDataSource
import com.youkydesign.sakeca.domain.groceries.Grocery
import com.youkydesign.sakeca.domain.groceries.IGroceriesRepository
import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceriesRepositoryImpl @Inject constructor(private val dataSource: GroceriesDataSource) :
    IGroceriesRepository {

    override fun getAll(): Flow<UiResource<List<Grocery>>> = flow {
        val data = dataSource.getAll()
        val domainData = data.map { entity ->
            GroceriesDataMapper.mapEntityToDomain(entity)
        }
        emit(UiResource.Success(domainData))
    }.flowOn(Dispatchers.IO)

    override fun insert(grocery: Grocery) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = GroceriesDataMapper.mapDomainToEntity(grocery)
            Log.d("AddIngredientToShoppingBag", "Inserting entity: $entity")

            dataSource.insert(entity)
        }
    }

    override fun update(grocery: Grocery) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = GroceriesDataMapper.mapDomainToEntity(grocery)
            dataSource.update(entity)
        }
    }

    override fun delete(grocery: Grocery) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = GroceriesDataMapper.mapDomainToEntity(grocery)
            dataSource.delete(entity)
        }
    }

    override fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.deleteAll()
        }
    }
}