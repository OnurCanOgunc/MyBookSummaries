package com.decode.mybooksummaries.core.utils

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Failure(val message: String) : AuthResponse
}