package com.decode.mybooksummaries.presentation.profile

object ProfileContract {
    data class UiState(
        val email: String = "",
        val username: String = "",
        val avatar: String = "",
        val booksReadThisMonth: String = "0",
        val monthlyBookGoal: Int = 0,
        val totalBooksRead: String = "0",
        val errorMessage: String = "",
        val profileImage: ProfileImageState = ProfileImageState.Default,
    )

    sealed interface UiAction {
        data class ProfileImageSelected(val image: ProfileImageState) : UiAction
        data object LogOut : UiAction
        data object NavigateEditProfile : UiAction
        data object OnBackClick : UiAction

    }
    sealed interface UiEffect {
        data object NavigateToWelcome : UiEffect
        data object NavigateEditProfile : UiEffect
        data object NavigateBack : UiEffect
    }
}