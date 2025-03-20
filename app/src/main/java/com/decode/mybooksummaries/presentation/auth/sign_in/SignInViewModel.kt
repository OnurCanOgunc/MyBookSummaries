package com.decode.mybooksummaries.presentation.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.usecase.AuthUseCases
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInContract.UiAction
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInContract.UiEffect
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel<UiState, UiAction, UiEffect>(
        connectivityObserver,
        UiState()
    ) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.OnEmailChange -> updateUiState { copy(email = uiAction.email) }
                is UiAction.OnPasswordChange -> updateUiState { copy(password = uiAction.password) }
               UiAction.OnSignInClick -> signIn()
               UiAction.OnSignUpClick -> emitUiEffect(UiEffect.NavigateSignUp)
               UiAction.OnSendRestPasswordEmailClick -> sendRestPasswordEmail()
               UiAction.OnMessageShown -> updateUiState { copy(message = "") }
               UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
               UiAction.OnDialogDismiss -> updateUiState { copy(dialogVisible = false) }
               UiAction.OnResetPasswordClick -> updateUiState { copy(dialogVisible = true) }
               UiAction.OnPasswordVisibilityClick -> updateUiState {
                    copy(
                        passwordVisibility = !passwordVisibility
                    )
                }
            }
        }
    }

    private fun sendRestPasswordEmail() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        authUseCases.resetPasswordUseCase(uiState.value.email).collectLatest { response ->
            when (response) {
                is AuthResponse.Success -> {
                    updateUiState {
                        copy(
                            isLoading = false,
                            message = "Password reset email has been sent."
                        )
                    }
                }

                is AuthResponse.Failure -> {
                    updateUiState { copy(isLoading = false, message = response.message) }
                }
            }
        }
    }

    private fun signIn() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val response =
            authUseCases.loginUseCase(uiState.value.email, uiState.value.password).first()) {
            is AuthResponse.Success -> {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.NavigateHome)
            }

            is AuthResponse.Failure -> {
                updateUiState { copy(isLoading = false, message = response.message) }
            }
        }
    }

}