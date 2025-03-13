package com.decode.mybooksummaries.presentation.edit_profile

object EditProfileContract {
    data class UiState(
        val displayName: String = "",
        val email: String = "",
        val currentPassword: String = "",
        val newPassword: String = "",
        val monthlyBookGoal: Int = 0,
        val message: String? = null,
        val passwordVisibilityCurrent: Boolean = false,
        val passwordVisibilityNew: Boolean = false,
        val isLoading: Boolean = false
    )
    sealed interface UiAction{
        data object OnSaveClick : UiAction
        data object OnBackClick : UiAction
        data class OnDisplayNameChange(val displayName: String) : UiAction
        data class OnCurrentPasswordChange(val currentPassword: String) : UiAction
        data class OnNewPasswordChange(val newPassword: String) : UiAction
        data class OnMonthlyBookGoalChange(val monthlyBookGoal: Int) : UiAction
        object OnPasswordVisibilityCurrentClick : UiAction
        object OnPasswordVisibilityNewClick : UiAction
    }
    sealed interface UiEffect {
        data object NavigateBack : UiEffect

    }
}