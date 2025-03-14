package com.decode.mybooksummaries.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : AuthRepository {

    override val currentUser get() = auth.currentUser

    override fun createAccountWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String
    ): Flow<AuthResponse> =
        callbackFlow {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                    auth.currentUser?.updateProfile(profileUpdate)
                        ?.addOnCompleteListener { updateTask ->
                            if (!updateTask.isSuccessful) {
                                Log.e(updateTask.exception?.message, "Profile update error")
                            }
                        }
                    trySend(AuthResponse.Success)
                } else {
                    val errorMessage = handleFirebaseAuthException(task.exception)
                    trySend(AuthResponse.Failure(errorMessage))
                }
            }
            awaitClose()
        }.catch { e ->
            Log.e(e.message, "Unexpected error while registering")
            emit(AuthResponse.Failure(context.getString(R.string.error_unknown)))
        }


    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResponse> =
        callbackFlow {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    val errorMessage = handleFirebaseAuthException(task.exception)
                    trySend(AuthResponse.Failure(errorMessage))
                }
            }
            awaitClose()
        }.catch { e ->
            Log.e(e.message, "Unexpected error during login")
            emit(AuthResponse.Failure(context.getString(R.string.error_unknown)))
        }

    override fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        try {
            val googleIdToken = getGoogleIdToken()
                ?: throw Exception(context.getString(R.string.error_google_auth_failed))
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    val errorMessage = handleFirebaseAuthException(task.exception)
                    trySend(AuthResponse.Failure(errorMessage))
                }
            }
        } catch (e: Exception) {
            trySend(AuthResponse.Failure(e.message ?: context.getString(R.string.error_unknown)))
        }
        awaitClose()
    }.catch { e ->
        Log.e(e.message, "Unexpected error while signing in with Google!")
        emit(AuthResponse.Failure(context.getString(R.string.error_unknown)))
    }

    private suspend fun getGoogleIdToken(): String? {

        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .setAutoSelectEnabled(false)
                .setNonce(createNonce())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context, request)

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                GoogleIdTokenCredential.createFrom(credential.data).idToken
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Authentication error: ${e.message}")
            null
        }
    }


    override suspend fun resetPassword(email: String): AuthResponse {
        return try {
            auth.sendPasswordResetEmail(email)
            AuthResponse.Success
        } catch (e: Exception) {
            val errorMessage = handleFirebaseAuthException(e)
            AuthResponse.Failure(errorMessage)
        }
    }

    override suspend fun signOut(): AuthResponse {
        return try {
            auth.signOut()
            AuthResponse.Success
        } catch (e: Exception) {
            val errorMessage = handleFirebaseAuthException(e)
            AuthResponse.Failure(errorMessage)
        }
    }

    override suspend fun updateDisplayName(newDisplayName: String): AuthResponse {
        val user = auth.currentUser ?: return AuthResponse.Failure("No User")

        return try {
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build()

            user.updateProfile(profileUpdate).await()
            AuthResponse.Success
        } catch (e: Exception) {
            val errorMessage = handleFirebaseAuthException(e)
            AuthResponse.Failure(errorMessage)
        }
    }

    override suspend fun updatePassword(newPassword: String,currentPassword: String): AuthResponse {
        val user = auth.currentUser ?: return AuthResponse.Failure("No User")

        return try {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()

            user.updatePassword(newPassword).await()
            AuthResponse.Success
        } catch (e: Exception) {
            val errorMessage = handleFirebaseAuthException(e)
            AuthResponse.Failure(errorMessage)
        }
    }


    private fun handleFirebaseAuthException(e: Exception?): String {
        return when (e) {
            is FirebaseAuthUserCollisionException -> context.getString(R.string.error_email_in_use)
            is FirebaseAuthWeakPasswordException -> context.getString(R.string.error_weak_password)
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.error_invalid_email)
            is FirebaseAuthInvalidUserException -> context.getString(R.string.error_user_not_found)
            else -> {
                context.getString(R.string.error_unknown)
            }
        }
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}