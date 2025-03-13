package com.decode.mybooksummaries.core.utils

sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Failure(val message: String? = null) : Response<Nothing>()
    object Empty : Response<Nothing>()
}