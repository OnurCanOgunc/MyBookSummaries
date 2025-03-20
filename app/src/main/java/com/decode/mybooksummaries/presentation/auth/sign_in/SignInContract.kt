package com.decode.mybooksummaries.presentation.auth.sign_in

object SignInContract {
    data class UiState(
        val email: String = "",
        val password: String = "",
        val dialogVisible: Boolean = false,
        val isLoading: Boolean = false,
        val passwordVisibility: Boolean = false,
        val message: String = ""
    )
    sealed interface UiAction {
        data class OnEmailChange(val email: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        data object OnDialogDismiss : UiAction
        data object OnSignInClick : UiAction
        data object OnSignUpClick : UiAction
        data object OnResetPasswordClick : UiAction
        data object OnSendRestPasswordEmailClick : UiAction
        data object OnPasswordVisibilityClick : UiAction
        data object OnMessageShown : UiAction
        data object OnBackClick : UiAction
    }
    sealed interface UiEffect {
        data object NavigateHome : UiEffect
        data object NavigateSignUp : UiEffect
        data object NavigateBack : UiEffect

    }
}