package com.decode.mybooksummaries.presentation.onboarding

import androidx.annotation.DrawableRes
import com.decode.mybooksummaries.R

sealed class OnboardingModel(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
) {
    data object FirstPage: OnboardingModel(
        image = R.drawable.img1,
        title = "Welcome to Your Book World!",
        description = "Manage your books effortlessly, add summaries and quotes, and elevate your reading experience."
    )
    data object SecondPage: OnboardingModel(
        image = R.drawable.img2,
        title = "Manage Your Books Easily",
        description = "Create your book library, categorize your books, and track their reading status. Never lose your books again!"
    )
    data object ThirdPage: OnboardingModel(
        image = R.drawable.img3,
        title = "Summaries and Quotes",
        description = "Save summaries of the books you've read and keep your favorite quotes. Access information easily anytime!"
    )
    data object FourthPage: OnboardingModel(
        image = R.drawable.img5,
        title = "Let's Get Started!",
        description = "Ready to build your book library? Click the 'Start' button and transform your reading experience!"
    )
}