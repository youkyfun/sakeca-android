package com.youkydesign.core

import com.youkydesign.core.domain.UiResource
import com.youkydesign.core.data.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Suppress("EmptyMethod")
abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<UiResource<ResultType>> = flow {
        emit(UiResource.Loading())
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            emit(UiResource.Loading())
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { UiResource.Success(it) })
                }

                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map { UiResource.Success(it) })
                }

                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(UiResource.Error<ResultType>(apiResponse.errorMessage))
                }
            }
        } else {
            emitAll(loadFromDB().map { UiResource.Success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<UiResource<ResultType>> = result
}