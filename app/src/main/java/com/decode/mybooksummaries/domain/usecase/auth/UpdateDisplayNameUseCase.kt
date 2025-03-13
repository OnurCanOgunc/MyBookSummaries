package com.decode.mybooksummaries.domain.usecase.auth

import com.decode.mybooksummaries.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateDisplayNameUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(displayName: String) = authRepository.updateDisplayName(displayName)
}