package com.decode.mybooksummaries.domain.usecase.auth

import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthResponse> {
        return authRepository.signInWithGoogle()
    }
}