package com.youkydesign.sakeca.core

import com.youkydesign.sakeca.data.groceries.GroceriesDataSource
import com.youkydesign.sakeca.domain.groceries.Grocery
import com.youkydesign.sakeca.domain.groceries.IGroceriesRepository
import com.youkydesign.sakeca.utils.UiResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceriesRepositoryImpl @Inject constructor(private val dataSource: GroceriesDataSource) :
    IGroceriesRepository {
    val scope = CoroutineScope(Dispatchers.IO)

    override fun getAll(): Flow<UiResource<List<Grocery>>> = flow {
        scope.launch {
            val data = dataSource.getAll()
            data.map { entity ->
                GroceriesDataMapper.mapEntityToDomain(entity)
            }
        }
    }

    override fun insert(grocery: Grocery) {
        scope.launch {
            val entity = GroceriesDataMapper.mapDomainToEntity(grocery)
            dataSource.insert(entity)
        }
    }

    override fun update(grocery: Grocery) {
        scope.launch {
            val entity = GroceriesDataMapper.mapDomainToEntity(grocery)
            dataSource.update(entity)
        }
    }

    override fun delete(grocery: Grocery) {
        scope.launch {
            val entity = GroceriesDataMapper.mapDomainToEntity(grocery)
            dataSource.delete(entity)
        }
    }

    override fun deleteAll() {
        scope.launch {
            dataSource.deleteAll()
        }
    }

}