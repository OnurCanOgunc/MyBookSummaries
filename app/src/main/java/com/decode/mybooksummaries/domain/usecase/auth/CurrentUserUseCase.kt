package com.decode.mybooksummaries.domain.usecase.auth

import com.decode.mybooksummaries.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun getCurrentUser(): FirebaseUser? {
        return authRepository.currentUser
    }
}