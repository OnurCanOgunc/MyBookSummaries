package com.decode.mybooksummaries.presentation.auth.sign_in

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.components.CustomBackButton
import com.decode.mybooksummaries.core.ui.components.CustomOutlinedTextField
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInContract.UiAction
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInContract.UiEffect
import com.decode.mybooksummaries.presentation.auth.sign_in.SignInContract.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    popBackStack: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var isMessageVisible by remember { mutableStateOf(false) }

    uiEffect.CollectWithLifecycle {
        when (it) {
            UiEffect.NavigateHome -> navigateToHome()
            UiEffect.NavigateSignUp -> navigateToSignUp()
            UiEffect.NavigateBack -> popBackStack()
        }
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotEmpty()) {
            isMessageVisible = true
            delay(3000)
            isMessageVisible = false
            delay(600)
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
                .padding(16.dp)
                .padding(top = 80.dp)
        ) {
            SignInHeader()
            SignInFields(uiState, onAction)
            AnimatedVisibility(
                visible = isMessageVisible,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }
                ) + fadeOut()
            ) {
                Text(
                    text = uiState.message,
                    color = CustomTheme.colors.errorColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    style = CustomTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold

                    )
            }

            SignInButton { onAction(UiAction.OnSignInClick) }
        }

        SignInFooter(modifier = Modifier.align(Alignment.BottomCenter)) { onAction(UiAction.OnSignUpClick) }
    }

    ForgotPasswordModal(uiState, onAction, sheetState)
}

@Composable
fun SignInHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.hello),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = CustomTheme.typography.displaySmall,
            color = CustomTheme.colors.textBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.glad_to_see_you_again),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = CustomTheme.typography.bodyLarge,
            color = CustomTheme.colors.textBlack,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SignInFields(uiState: UiState, onAction: (UiAction) -> Unit) {

    Column {
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

        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier
                .align(Alignment.End)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onAction(UiAction.OnResetPasswordClick) },
            color = CustomTheme.colors.textBlack,
            style = CustomTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SignInButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CustomTheme.colors.charcoalBlack,
            contentColor = CustomTheme.colors.softWhite
        )
    ) {
        Text(stringResource(R.string.sign_in), style = CustomTheme.typography.labelLarge)
    }
}

@Composable
fun SignInFooter(modifier: Modifier, onSignUpClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.no_account),
            color = CustomTheme.colors.textBlack,
            style = CustomTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.create_account),
            color = CustomTheme.colors.textBlack,
            fontWeight = FontWeight.Bold,
            style = CustomTheme.typography.bodyMedium,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onSignUpClick() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordModal(uiState: UiState, onAction: (UiAction) -> Unit, sheetState: SheetState) {
    if (uiState.dialogVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onAction(UiAction.OnDialogDismiss) },
            containerColor = CustomTheme.colors.backgroundColor,
            tonalElevation = 8.dp
        ) {
            ForgotPasswordSheet(
                resetEmail = uiState.email,
                onEmailChange = { onAction(UiAction.OnEmailChange(it)) },
                onDismiss = { onAction(UiAction.OnDialogDismiss) },
                onResetPassword = {
                    onAction(UiAction.OnDialogDismiss)
                    onAction(UiAction.OnSendRestPasswordEmailClick)
                }
            )
        }
    }
}

@Composable
fun ForgotPasswordSheet(
    resetEmail: String = "",
    onEmailChange: (String) -> Unit = {},
    onDismiss: () -> Unit,
    onResetPassword: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.forgot_password),
            style = CustomTheme.typography.headlineSmall,
            color = CustomTheme.colors.textBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.reset_password),
            textAlign = TextAlign.Center,
            style = CustomTheme.typography.bodyMedium,
            color = CustomTheme.colors.textBlack
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            value = resetEmail,
            onValueChange = onEmailChange,
            label = stringResource(R.string.email_address),
            keyboardType = KeyboardType.Email,
            icon = Icons.Outlined.Email
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onResetPassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(
                stringResource(R.string.reset_password),
                color = CustomTheme.colors.softWhite,
                style = CustomTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onDismiss) {
            Text(
                stringResource(R.string.cancel),
                color = CustomTheme.colors.errorColor,
                style = CustomTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}