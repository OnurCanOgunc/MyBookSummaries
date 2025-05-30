package com.decode.mybooksummaries.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.decode.mybooksummaries.navigation.nav_graph.authGraph
import com.decode.mybooksummaries.navigation.nav_graph.mainGraph
import com.decode.mybooksummaries.presentation.onboarding.OnboardingScreen
import com.decode.mybooksummaries.presentation.onboarding.OnboardingUtils

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onboardingUtils: OnboardingUtils,
    isLoggedIn: Boolean,
) {
    val startDestination = remember {
        if (onboardingUtils.isOnboardingCompleted()) {
            if (isLoggedIn == false) Screens.Main else Screens.Auth
        } else {
            Screens.Onboarding
        }
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screens.Onboarding> {
            OnboardingScreen(onFinished = {
                onboardingUtils.setOnboardingCompleted()
                navController.navigate(Screens.Auth.Welcome) {
                    popUpTo(Screens.Onboarding) { inclusive = true }
                }
            })
        }
        authGraph(navController)
        mainGraph(navController)
    }
}