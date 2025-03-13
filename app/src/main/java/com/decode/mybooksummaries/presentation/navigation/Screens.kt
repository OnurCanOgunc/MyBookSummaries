package com.decode.mybooksummaries.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object Onboarding

    @Serializable
    object Main {
        @Serializable
        data object Home
        @Serializable
        data class AddBook(val bookId: String? = null)
        @Serializable
        data class BookDetail(val bookId: String)
    }

    @Serializable
    object Auth {
        @Serializable
        data object Welcome
        @Serializable
        data object SignIn
        @Serializable
        data object SignUp
    }
}