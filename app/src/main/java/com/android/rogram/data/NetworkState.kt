package com.android.rogram.data

sealed class NetworkState<T>(
    val data: T? = null,
    val message: String? = null,
    val e: Throwable? = null
) {
    class Success<T>(data: T) : NetworkState<T>(data)
    class Loading<T> : NetworkState<T>()
    class Error<T>( e: Throwable) : NetworkState<T>()
}