package com.decode.mybooksummaries.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.decode.mybooksummaries.presentation.home.HomeScreen
import com.decode.mybooksummaries.presentation.onboarding.OnboardingScreen
import com.decode.mybooksummaries.presentation.onboarding.OnboardingUtils

@Composable
fun NavGraph(
    navController: NavHostController,
    onboardingUtils: OnboardingUtils,
) {
    val startDestination = remember {
        if (onboardingUtils.isOnboardingCompleted()) {
            Screens.Main.Home
        } else {
            Screens.Onboarding
        }
    }


    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screens.Onboarding> {
            OnboardingScreen(onFinished = {
                onboardingUtils.setOnboardingCompleted()
                navController.navigate(Screens.Main.Home) {
                    popUpTo(Screens.Onboarding) { inclusive = true }
                }
            })
        }
        composable<Screens.Main.Home> {
            HomeScreen()
        }
    }

}