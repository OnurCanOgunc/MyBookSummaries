package com.decode.mybooksummaries.domain.usecase.auth

import android.util.Patterns
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email: String): Flow<AuthResponse> = flow {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emit(AuthResponse.Failure("Invalid email address"))
            return@flow
        }
        val response = authRepository.resetPassword(email)
        when (response) {
            is AuthResponse.Success -> emit(AuthResponse.Success)
            is AuthResponse.Failure -> emit(AuthResponse.Failure(response.message))
        }
    }
}