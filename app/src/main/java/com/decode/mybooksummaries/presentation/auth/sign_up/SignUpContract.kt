package com.decode.mybooksummaries.presentation.auth.sign_up

object SignUpContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val username: String = "",
        val password: String = "",
        val passwordAgain: String = "",
        val message: String = "",
    )
    sealed interface UiAction {
        data object OnLoginClick : UiAction
        data object OnSignUpClick : UiAction
        data class OnEmailChange(val email: String) : UiAction
        data class OnUsernameChange(val username: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        data class OnPasswordAgainChange(val passwordAgain: String) : UiAction
        data object OnBackClick : UiAction
        data object OnMessageShown : UiAction
    }
    sealed interface UiEffect {
        data object NavigateBack  : UiEffect
        data object NavigateSignIn : UiEffect
        data object NavigateHome : UiEffect
    }
}