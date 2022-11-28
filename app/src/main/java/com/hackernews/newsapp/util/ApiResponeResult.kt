package com.hackernews.newsapp.util

sealed class ApiResponeResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T): ApiResponeResult<T>(data)
    class Error<T>(message: String?, data: T? = null): ApiResponeResult<T>(data,message)
    class Loading<T>: ApiResponeResult<T>()

}
