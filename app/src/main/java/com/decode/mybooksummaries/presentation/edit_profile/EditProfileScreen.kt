package com.decode.mybooksummaries.presentation.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.components.CustomOutlinedTextField
import com.decode.mybooksummaries.core.ui.components.TopBar
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileContract.UiAction
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileContract.UiEffect
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileContract.UiState
import com.decode.mybooksummaries.presentation.edit_profile.component.ProfileInfoCard
import com.decode.mybooksummaries.presentation.edit_profile.component.ReadingGoalSlider
import kotlinx.coroutines.flow.Flow

@Composable
fun EditProfileScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onBackClick: () -> Unit
) {

    uiEffect.CollectWithLifecycle {
        if (it is UiEffect.NavigateBack) onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        TopBar(
            title = stringResource(R.string.edit_profile),
            popBackStack = { onAction(UiAction.OnBackClick) })

        Spacer(modifier = Modifier.height(20.dp))

        ProfileInfoCard(email = uiState.email)

        Spacer(modifier = Modifier.height(24.dp))

        PasswordChange(uiState, onAction)

        Spacer(modifier = Modifier.height(24.dp))

        ReadingGoalSlider(uiState, onAction)

        Spacer(modifier = Modifier.weight(1f))

        ActionButtons(onAction)
    }
}

@Composable
private fun PasswordChange(
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Text(stringResource(R.string.change_password), style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
    Spacer(modifier = Modifier.height(8.dp))
    CustomOutlinedTextField(
        value = uiState.currentPassword,
        onValueChange = { onAction(UiAction.OnCurrentPasswordChange(it)) },
        label = stringResource(R.string.current_password),
        isPassword = true,
        passwordVisibility = uiState.passwordVisibilityCurrent,
        onPasswordVisibilityToggle = { onAction(UiAction.OnPasswordVisibilityCurrentClick) },
        icon = Icons.Default.Lock,
        colors = TextFieldDefaults.colors(
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedLabelColor = Color.LightGray,
            unfocusedLabelColor = Color.LightGray,
        )

    )
    Spacer(modifier = Modifier.height(8.dp))
    CustomOutlinedTextField(
        value = uiState.newPassword,
        onValueChange = { onAction(UiAction.OnNewPasswordChange(it)) },
        label = stringResource(R.string.new_password),
        isPassword = true,
        passwordVisibility = uiState.passwordVisibilityNew,
        onPasswordVisibilityToggle = { onAction(UiAction.OnPasswordVisibilityNewClick) },
        icon = Icons.Default.Lock,
        colors = TextFieldDefaults.colors(
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = Color.White,
            focusedPlaceholderColor = Color.LightGray,
            unfocusedPlaceholderColor = Color.LightGray,
            focusedLabelColor = Color.LightGray,
            unfocusedLabelColor = Color.LightGray,
        )
    )
}

@Composable
fun ActionButtons(onAction: (UiAction) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(onClick = { onAction(UiAction.OnBackClick) }) {
            Text(stringResource(R.string.cancel), color = Color.White)
        }
        Button(
            onClick = { onAction(UiAction.OnSaveClick) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(stringResource(R.string.save_changes), color = Color.White)
        }
    }
}



