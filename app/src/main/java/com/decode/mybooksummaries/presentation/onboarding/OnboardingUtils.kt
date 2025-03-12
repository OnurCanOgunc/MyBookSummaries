package com.decode.mybooksummaries.presentation.onboarding

import android.content.Context
import androidx.core.content.edit

class OnboardingUtils(private val context: Context) {

    fun isOnboardingCompleted(): Boolean {
        return context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .getBoolean("completed", false)
    }

    fun setOnboardingCompleted() {
        context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .edit() {
                putBoolean("completed", true)
            }
    }
}