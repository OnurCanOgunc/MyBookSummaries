package com.decode.mybooksummaries.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object Onboarding

    @Serializable
    sealed interface Main: Screens {
        @Serializable
        data object Home
    }
}