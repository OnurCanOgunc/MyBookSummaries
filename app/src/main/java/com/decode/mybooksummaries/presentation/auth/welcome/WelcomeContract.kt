package com.decode.mybooksummaries.presentation.auth.welcome

object WelcomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val message: String = "",
    )

    sealed interface UiAction {
        data object OnLoginClick : UiAction
        data object OnSignUpClick : UiAction
        data object OnLoginWithGoogleClick : UiAction
    }

    sealed interface UiEffect {
        data object NavigateLogin : UiEffect
        data object NavigateHome : UiEffect
        data object NavigateSignUp : UiEffect
    }
}