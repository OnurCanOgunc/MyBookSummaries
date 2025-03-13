package com.decode.mybooksummaries.presentation.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import okhttp3.internal.immutableListOf

object ProfileContract {
    @Immutable
    data class UiState(
        val email: String = "",
        val username: String = "",
        val booksReadThisMonth: String = "0",
        val monthlyBookGoal: Int = 0,
        val totalBooksRead: String = "0",
        val errorMessage: String? = null,
        val icons: List<ImageVector> = immutableListOf(
            Icons.Default.EmojiEvents,
            Icons.AutoMirrored.Default.MenuBook,
            Icons.Default.Star
        )
    )

    sealed interface UiAction {
        object LogOut : UiAction
        object NavigateEditProfile : UiAction
        object OnBackClick : UiAction

    }
    sealed interface UiEffect {
        object NavigateToWelcome : UiEffect
        object NavigateEditProfile : UiEffect
        object NavigateBack : UiEffect
    }
}