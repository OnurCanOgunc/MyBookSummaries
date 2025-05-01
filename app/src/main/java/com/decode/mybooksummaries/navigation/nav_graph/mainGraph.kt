package com.decode.mybooksummaries.navigation.nav_graph

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.decode.mybooksummaries.presentation.addbook.AddBookScreen
import com.decode.mybooksummaries.presentation.addbook.AddBookViewModel
import com.decode.mybooksummaries.presentation.detail.DetailScreen
import com.decode.mybooksummaries.presentation.detail.DetailViewModel
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileScreen
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileViewModel
import com.decode.mybooksummaries.presentation.home.HomeScreen
import com.decode.mybooksummaries.presentation.home.HomeViewModel
import com.decode.mybooksummaries.navigation.Screens
import com.decode.mybooksummaries.presentation.profile.ProfileScreen
import com.decode.mybooksummaries.presentation.profile.ProfileViewModel

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation<Screens.Main>(startDestination = Screens.Main.Home) {
        composable<Screens.Main.Home>{
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                onAction = viewModel::onAction,
                uiEffect = uiEffect,
                onAddClick = { navController.navigate(Screens.Main.AddBook()) },
                onDetailClick = { navController.navigate(Screens.Main.BookDetail(it))},
                onProfileClick = { navController.navigate(Screens.Main.Profile) },
            )
        }
        composable<Screens.Main.AddBook> {
            val arguments = it.toRoute<Screens.Main.AddBook>()
            val viewModel = hiltViewModel<AddBookViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            AddBookScreen(
                bookId = arguments.bookId,
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                popBackStack = { navController.popBackStack() })
        }
        composable<Screens.Main.BookDetail> { backStackEntry ->
            val arguments = backStackEntry.toRoute<Screens.Main.BookDetail>()
            val viewModel = hiltViewModel<DetailViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            DetailScreen(
                bookId = arguments.bookId,
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                popBackStack = { navController.popBackStack() },
                onAddBookClick = {
                    navController.navigate(Screens.Main.AddBook(it))
                },
            )
        }
        composable<Screens.Main.Profile> {
            val viewModel = hiltViewModel<ProfileViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            ProfileScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateWelcome = {
                    navController.navigate(Screens.Auth.Welcome) {
                        popUpTo(Screens.Main) {
                            inclusive = true
                        }
                    }
                }
                ,
                onEditProfileClick = {
                    navController.navigate(Screens.Main.EditProfile)
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<Screens.Main.EditProfile>(
            deepLinks = listOf(
                navDeepLink<Screens.Main.EditProfile>(
                    basePath = "myapp://edit_profile"
                )
            )
        ) {
            val viewModel = hiltViewModel<EditProfileViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            EditProfileScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}