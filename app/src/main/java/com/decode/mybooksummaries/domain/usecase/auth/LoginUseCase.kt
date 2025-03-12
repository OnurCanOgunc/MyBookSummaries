package com.decode.mybooksummaries.domain.usecase.auth

import android.util.Patterns
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<AuthResponse> = flow {
        if (email.isEmpty() || password.isEmpty()) {
            emit(AuthResponse.Failure("Lütfen tüm alanları doldurun"))
            return@flow
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emit(AuthResponse.Failure("Geçersiz e-posta adresi"))
            return@flow
        }
        if (password.length < 6) {
            emit(AuthResponse.Failure("Şifre en az 6 karakter olmalıdır"))
            return@flow
        }
        emitAll(authRepository.loginWithEmailAndPassword(email, password))
    }
}