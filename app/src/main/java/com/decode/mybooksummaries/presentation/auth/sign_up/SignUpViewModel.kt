package com.decode.mybooksummaries.presentation.auth.sign_up

import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.usecase.AuthUseCases
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpContract.UiAction
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpContract.UiEffect
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
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
                is UiAction.OnPasswordChange -> updateUiState {
                    copy(
                        password = uiAction.password
                    )
                }

                is UiAction.OnPasswordAgainChange -> updateUiState {
                    copy(
                        passwordAgain = uiAction.passwordAgain
                    )
                }

                is UiAction.OnUsernameChange -> updateUiState {
                    copy(
                        username = uiAction.username
                    )
                }

                UiAction.OnSignUpClick -> onSignUpClick()
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                UiAction.OnLoginClick -> emitUiEffect(UiEffect.NavigateSignIn)
                UiAction.OnMessageShown -> updateUiState { copy(message = "") }
                UiAction.OnPasswordVisible1Click -> updateUiState {
                    copy(
                        passwordVisible1 = !passwordVisible1
                    )
                }
                UiAction.OnPasswordVisible2Click -> updateUiState {
                    copy(
                        passwordVisible2 = !passwordVisible2
                    )
                }
            }
        }
    }

    private fun onSignUpClick() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        val email = uiState.value.email
        val password = uiState.value.password
        val username = uiState.value.username
        val passwordAgain = uiState.value.passwordAgain

        if (password != passwordAgain) {
            updateUiState { copy(isLoading = false, message = "Passwords Do Not Match") }
            return@launch
        }

        when (val response = authUseCases.createAccountUseCase(username, email, password).first()) {
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