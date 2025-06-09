package com.youkydesign.sakeca.utils

sealed class UiResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : UiResource<T>(data)
    class Loading<T>(data: T? = null) : UiResource<T>(data)
    class Error<T>(message: String, data: T? = null) : UiResource<T>(data, message)
    class Idle<T> : UiResource<T>()
}