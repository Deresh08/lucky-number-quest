package com.gaming.oddsgame.lib

open class ServiceResult<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : ServiceResult<T>(data)
    class Error<T>(message: String,) : ServiceResult<T>(null, message)
}
