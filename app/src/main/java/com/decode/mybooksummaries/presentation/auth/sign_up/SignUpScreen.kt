package com.decode.mybooksummaries.presentation.auth.sign_up

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.components.CustomOutlinedTextField
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.ErrorColor
import com.decode.mybooksummaries.core.ui.theme.HomeBackgroundColor2
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
        if (!uiState.message.isNullOrEmpty()) {
            delay(3000)
            onAction(UiAction.OnMessageShown)
        }
    }

    Box(modifier = modifier.fillMaxSize().background(HomeBackgroundColor2)) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = { onAction(UiAction.OnBackClick) },
            modifier = Modifier.padding(top = 24.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

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
            if (!uiState.message.isNullOrEmpty()) {
                AnimatedVisibility(visible = true) {
                    Text(
                        text = uiState.message,
                        color = ErrorColor,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
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
            text = "Hoşgeldin",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Keşfetmeye Başlayın!",
            fontSize = 18.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun SignUpFields(uiState: UiState, onAction: (UiAction) -> Unit) {
    Column {
        CustomOutlinedTextField(
            value = uiState.username,
            onValueChange = { onAction(UiAction.OnUsernameChange(it)) },
            label = "Kullanıcı Adı",
            icon = Icons.Outlined.Person
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            value = uiState.email,
            onValueChange = { onAction(UiAction.OnEmailChange(it)) },
            label = "Eposta adresi",
            keyboardType = KeyboardType.Email,
            icon = Icons.Outlined.Email
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            value = uiState.password,
            onValueChange = { onAction(UiAction.OnPasswordChange(it)) },
            label = "Şifre",
            isPassword = true,
            icon = Icons.Outlined.Lock,
            passwordVisibility = uiState.passwordVisible1,
            onPasswordVisibilityToggle = { onAction(UiAction.OnPasswordVisible1Click) }
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            value = uiState.passwordAgain,
            onValueChange = { onAction(UiAction.OnPasswordAgainChange(it)) },
            label = "Şifreyi Tekrarla",
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
            containerColor = Color(0xFF4CAF50),
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text("Hesap Oluştur", color = Color.White)
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
            text = "Hesabın zaten var mı?",
            color = Color.Black.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Giriş Yap",
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onLoginClick() }
        )
    }
}

