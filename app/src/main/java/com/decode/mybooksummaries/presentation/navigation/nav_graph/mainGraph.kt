package com.decode.mybooksummaries.presentation.navigation.nav_graph

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.decode.mybooksummaries.presentation.home.HomeScreen
import com.decode.mybooksummaries.presentation.home.HomeViewModel
import com.decode.mybooksummaries.presentation.navigation.Screens

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation<Screens.Main>(startDestination = Screens.Main.Home) {
        composable<Screens.Main.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                onAction = viewModel::onAction,
                uiEffect = uiEffect,
                onAddClick = { navController.navigate(Screens.Main.AddBook) },
            )
        }
    }
}