package com.decode.mybooksummaries.presentation.auth.sign_up

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.components.CustomBackButton
import com.decode.mybooksummaries.core.ui.components.CustomOutlinedTextField
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpContract.UiAction
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpContract.UiEffect
import com.decode.mybooksummaries.presentation.auth.sign_up.SignUpContract.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToSignIn: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    popBackStack: () -> Unit = {}
) {

    uiEffect.CollectWithLifecycle {
        when (it) {
            UiEffect.NavigateBack -> popBackStack()
            UiEffect.NavigateSignIn -> navigateToSignIn()
            UiEffect.NavigateHome -> navigateToHome()
        }
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotEmpty()) {
            delay(3000)
            onAction(UiAction.OnMessageShown)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        CustomBackButton(back = { onAction(UiAction.OnBackClick) })

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(top = 80.dp)
        ) {
            SignUpHeader()
            SignUpFields(uiState, onAction)
            if (uiState.message.isNotEmpty()) {
                Text(
                    text = uiState.message,
                    color = CustomTheme.colors.errorColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                )
            }

            SignUpButton(uiState, onAction)
        }

        SignUpFooter(modifier = Modifier.align(Alignment.BottomCenter)) { onAction(UiAction.OnLoginClick) }
    }
}

@Composable
fun SignUpHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.welcome),
            style = CustomTheme.typography.headlineLarge,
            color = CustomTheme.colors.textBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.start_exploring),
            style = CustomTheme.typography.titleMedium,
            color = CustomTheme.colors.textBlack
        )
    }
}

@Composable
fun SignUpFields(uiState: UiState, onAction: (UiAction) -> Unit) {
    Column {
        CustomOutlinedTextField(
            value = uiState.username,
            onValueChange = { onAction(UiAction.OnUsernameChange(it)) },
            label = stringResource(R.string.username),
            icon = Icons.Outlined.Person
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            value = uiState.email,
            onValueChange = { onAction(UiAction.OnEmailChange(it)) },
            label = stringResource(R.string.email_address),
            keyboardType = KeyboardType.Email,
            icon = Icons.Outlined.Email
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            value = uiState.password,
            onValueChange = { onAction(UiAction.OnPasswordChange(it)) },
            label = stringResource(R.string.password),
            isPassword = true,
            icon = Icons.Outlined.Lock,
            passwordVisibility = uiState.passwordVisible1,
            onPasswordVisibilityToggle = { onAction(UiAction.OnPasswordVisible1Click) }
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            value = uiState.passwordAgain,
            onValueChange = { onAction(UiAction.OnPasswordAgainChange(it)) },
            label = stringResource(R.string.repeat_password),
            isPassword = true,
            icon = Icons.Outlined.Lock,
            passwordVisibility = uiState.passwordVisible2,
            onPasswordVisibilityToggle = { onAction(UiAction.OnPasswordVisible2Click) }
        )
    }
}

@Composable
fun SignUpButton(uiState: UiState, onAction: (UiAction) -> Unit) {
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = { onAction(UiAction.OnSignUpClick) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CustomTheme.colors.charcoalBlack,
            disabledContentColor = CustomTheme.colors.softWhite
        )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = CustomTheme.colors.softWhite)
        } else {
            Text(stringResource(R.string.create_account), color = CustomTheme.colors.softWhite)
        }
    }
}

@Composable
fun SignUpFooter(modifier: Modifier, onLoginClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.already_have_an_account),
            color = CustomTheme.colors.textBlack
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.sign_in),
            color = CustomTheme.colors.textBlack,
            fontWeight = FontWeight.Bold,
            style = CustomTheme.typography.labelLarge,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onLoginClick() }
        )
    }
}

