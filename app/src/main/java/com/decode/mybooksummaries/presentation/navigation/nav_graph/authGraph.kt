package com.decode.mybooksummaries.presentation.navigation.nav_graph

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInScreen
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInViewModel
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpScreen
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpViewModel
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeScreen
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeViewModel
import com.decode.mybooksummaries.presentation.navigation.Screens

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Screens.Auth>(startDestination = Screens.Auth.Welcome) {
        composable<Screens.Auth.Welcome> {
            val viewModel = hiltViewModel<WelcomeViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            WelcomeScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToHome = {
                    navController.navigate(Screens.Main.Home) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToSignUp = {
                    navController.navigate(Screens.Auth.SignUp) {
                        popUpTo(Screens.Auth.SignUp) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToLogin = {
                    navController.navigate(Screens.Auth.SignIn) {
                        popUpTo(Screens.Auth.SignIn) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<Screens.Auth.SignIn> {
            val viewModel = hiltViewModel<SignInViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SignInScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToHome = {
                    navController.navigate(Screens.Main.Home) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToSignUp = {
                    navController.navigate(Screens.Auth.SignUp) {
                        popUpTo(Screens.Auth.SignUp) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                popBackStack = { navController.popBackStack() }

            )
        }
        composable<Screens.Auth.SignUp> {
            val viewModel = hiltViewModel<SignUpViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SignUpScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToHome = {
                    navController.navigate(Screens.Main.Home) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToSignIn = {
                    navController.navigate(Screens.Auth.SignIn) {
                        popUpTo(Screens.Auth.SignIn) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                popBackStack = { navController.popBackStack() }
            )
        }
    }
}