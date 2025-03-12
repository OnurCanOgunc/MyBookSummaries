package com.decode.mybooksummaries.presentation.auth.sign_in

object SignInContract {
    data class UiState(
        val email: String = "",
        val password: String = "",
        val dialogVisible: Boolean = false,
        val isLoading: Boolean = false,
        val passwordVisibility: Boolean = false,
        val message: String? = null
    )
    sealed interface UiAction {
        data class OnEmailChange(val email: String) : UiAction
        data class OnPasswordChange(val password: String) : UiAction
        object OnDialogDismiss : UiAction
        object OnSignInClick : UiAction
        object OnSignUpClick : UiAction
        object OnResetPasswordClick : UiAction
        object OnSendRestPasswordEmailClick : UiAction
        object OnPasswordVisibilityClick : UiAction
        object OnMessageShown : UiAction
        object OnBackClick : UiAction
    }
    sealed interface UiEffect {
        object NavigateHome : UiEffect
        object NavigateSignUp : UiEffect
        object NavigateBack : UiEffect

    }
}