package com.decode.mybooksummaries.presentation.auth.welcome

import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.domain.usecase.AuthUseCases
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeContract.UiAction
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeContract.UiEffect
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    connectivityObserver: ConnectivityObserver,
): BaseViewModel<UiState, UiAction, UiEffect>(
    connectivityObserver,
    UiState()
) {
    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnLoginClick -> emitUiEffect(UiEffect.NavigateLogin)
                UiAction.OnSignUpClick -> emitUiEffect(UiEffect.NavigateSignUp)
                UiAction.OnLoginWithGoogleClick -> loginWithGoogle()
            }
        }
    }

    private fun loginWithGoogle() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val response = authUseCases.loginWithGoogleUseCase().first()) {
            AuthResponse.Success -> {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.NavigateHome)
            }
            is AuthResponse.Failure -> {
                updateUiState { copy(isLoading = false,message = response.message) }
            }
        }
    }

}