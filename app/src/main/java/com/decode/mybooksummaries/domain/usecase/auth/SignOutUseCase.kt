package com.decode.mybooksummaries.domain.usecase.auth

import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): AuthResponse = authRepository.signOut()
}