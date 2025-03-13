package com.decode.mybooksummaries.domain.usecase.auth

import com.decode.mybooksummaries.domain.repository.AuthRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(newPassword: String, currentPassword: String) =
        authRepository.updatePassword(newPassword, currentPassword)

}