package com.decode.mybooksummaries.presentation.edit_profile

import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.utils.AuthResponse
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.usecase.AuthUseCases
import com.decode.mybooksummaries.domain.usecase.MonthlyGoalUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val monthlyGoalUseCases: MonthlyGoalUseCases,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel<EditProfileContract.UiState, EditProfileContract.UiAction, EditProfileContract.UiEffect>(
    connectivityObserver,
    EditProfileContract.UiState(
        email = authUseCases.currentUserUseCase.getCurrentUser()?.email.orEmpty(),
        displayName = authUseCases.currentUserUseCase.getCurrentUser()?.displayName.orEmpty()
    )
) {
    override fun onAction(action: EditProfileContract.UiAction) {
        viewModelScope.launch {
            when (action) {
                EditProfileContract.UiAction.OnBackClick -> emitUiEffect(EditProfileContract.UiEffect.NavigateBack)
                is EditProfileContract.UiAction.OnCurrentPasswordChange -> updateUiState {
                    copy(currentPassword = action.currentPassword)
                }

                is EditProfileContract.UiAction.OnDisplayNameChange -> updateUiState {
                    copy(displayName = action.displayName)
                }

                is EditProfileContract.UiAction.OnMonthlyBookGoalChange -> updateUiState {
                    copy(monthlyBookGoal = action.monthlyBookGoal)
                }

                is EditProfileContract.UiAction.OnNewPasswordChange -> updateUiState {
                    copy(newPassword = action.newPassword)
                }

                EditProfileContract.UiAction.OnSaveClick -> saveChanges()
                EditProfileContract.UiAction.OnPasswordVisibilityCurrentClick -> {
                    updateUiState { copy(passwordVisibilityCurrent = !passwordVisibilityCurrent) }
                }

                EditProfileContract.UiAction.OnPasswordVisibilityNewClick -> {
                    updateUiState { copy(passwordVisibilityNew = !passwordVisibilityNew) }
                }
            }
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            val state = uiState.value

            try {
                val nameUpdateResult = updateDisplayNameIfNeeded(state.displayName)
                val passwordUpdateResult = updatePasswordIfNeeded(state.currentPassword, state.newPassword)

                val failureMessage = nameUpdateResult.getFailureMessage().ifEmpty {
                    passwordUpdateResult.getFailureMessage()
                }

                if (failureMessage.isNotEmpty()) {
                    updateUiState { copy(message = failureMessage, isLoading = false) }
                    return@launch
                }

                saveMonthlyGoal()
                updateUiState { copy(isLoading = false) }
                emitUiEffect(EditProfileContract.UiEffect.NavigateBack)

            } catch (e: Exception) {
                updateUiState { copy(message = e.message.orEmpty(), isLoading = false) }
            }
        }
    }

    private suspend fun updateDisplayNameIfNeeded(displayName: String): AuthResponse {
        if (displayName.isEmpty()) return AuthResponse.Success
        return authUseCases.updateDisplayNameUseCase(displayName)
    }

    private suspend fun updatePasswordIfNeeded(
        currentPassword: String,
        newPassword: String
    ): AuthResponse {
        if (currentPassword.isEmpty() || newPassword.isEmpty()) return AuthResponse.Success
        return authUseCases.updatePasswordUseCase(currentPassword, newPassword)
    }

    private fun AuthResponse.getFailureMessage(): String {
        return if (this is AuthResponse.Failure) this.message else ""
    }

    private fun saveMonthlyGoal() {
        viewModelScope.launch {
            isConnected.collectLatest { connected ->
                val response =
                    monthlyGoalUseCases.saveMonthlyGoal(uiState.value.monthlyBookGoal, connected)
                if (response is Response.Failure) {
                    updateUiState { copy(message = response.message) }
                }
            }
        }
    }
}