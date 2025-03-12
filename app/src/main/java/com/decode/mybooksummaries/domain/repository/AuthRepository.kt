package com.decode.mybooksummaries.domain.repository

import com.decode.mybooksummaries.core.utils.AuthResponse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun createAccountWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String
    ): Flow<AuthResponse>

    val currentUser: FirebaseUser?

    fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResponse>
    fun signInWithGoogle(): Flow<AuthResponse>
    suspend fun resetPassword(email: String): AuthResponse
    suspend fun signOut(): AuthResponse
    suspend fun updateDisplayName(newDisplayName: String): AuthResponse
    suspend fun updatePassword(newPassword: String,currentPassword: String): AuthResponse

}