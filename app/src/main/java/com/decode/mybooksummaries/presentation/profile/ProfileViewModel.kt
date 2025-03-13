package com.decode.mybooksummaries.presentation.profile

import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.usecase.AuthUseCases
import com.decode.mybooksummaries.domain.usecase.BookUseCases
import com.decode.mybooksummaries.domain.usecase.MonthlyGoalUseCases
import com.decode.mybooksummaries.presentation.profile.ProfileContract.UiAction
import com.decode.mybooksummaries.presentation.profile.ProfileContract.UiEffect
import com.decode.mybooksummaries.presentation.profile.ProfileContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val bookUseCases: BookUseCases,
    private val monthlyGoalUseCases: MonthlyGoalUseCases,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel<UiState, UiAction, UiEffect>(
    connectivityObserver,
    UiState()
) {
    private val currentUser = authUseCases.currentUserUseCase.getCurrentUser()

    override fun onAction(action: UiAction) {
        viewModelScope.launch {
            when (action) {
                UiAction.LogOut -> logOut()
                UiAction.NavigateEditProfile -> {
                    emitUiEffect(UiEffect.NavigateEditProfile)
                }
                UiAction.OnBackClick -> {
                    emitUiEffect(UiEffect.NavigateBack)
                }
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            when (val response = authUseCases.signOutUseCase()) {
                is AuthResponse.Success -> {
                    emitUiEffect(UiEffect.NavigateToWelcome)
                }

                is AuthResponse.Failure -> {
                    updateUiState { copy(errorMessage = response.message) }
                }

            }
        }
    }

    private fun authUser() {
        viewModelScope.launch {
            updateUiState {
                copy(
                    email = currentUser?.email ?: "",
                    username = currentUser?.displayName ?: ""
                )
            }
        }
    }

    private fun totalBooksRead() {
        viewModelScope.launch {
            isConnected.collectLatest { connected ->
                val response = bookUseCases.getTotalBooksRead(connected = connected)
                updateUiState {
                    when (response) {
                        is Response.Success -> {
                            copy(totalBooksRead = response.data.toString())
                        }

                        is Response.Failure -> {
                            copy(errorMessage = response.message)
                        }

                        Response.Empty -> {
                            copy(totalBooksRead = "0")
                        }
                    }
                }
            }
        }
    }

    private fun getMonthlyGoal() {
        viewModelScope.launch {
            isConnected.collectLatest { connected ->
                monthlyGoalUseCases.getMonthlyGoal(isConnected = connected)
                    .collect { response ->
                        updateUiState{
                        when (response) {
                            is Response.Success -> {
                                copy(monthlyBookGoal = response.data.goalCount)
                            }

                            is Response.Failure -> {
                                copy(errorMessage = response.message)
                            }

                            Response.Empty -> {
                                copy(monthlyBookGoal = 0)
                            }
                        }
                        }
                    }
            }
        }
    }


    private fun booksReadThisMonth() {
        viewModelScope.launch {
            val response = bookUseCases.getBooksReadThisMonth()
            updateUiState {
                when (response) {
                    is Response.Success -> {
                        copy(booksReadThisMonth = response.data.toString())
                    }

                    is Response.Failure -> {
                        copy(errorMessage = response.message)
                    }

                    else -> {
                        copy(errorMessage = "Something went wrong")
                    }
                }
            }
        }
    }

    override suspend fun initialDataLoad() {
        authUser()
        totalBooksRead()
        booksReadThisMonth()
        getMonthlyGoal()
    }
}