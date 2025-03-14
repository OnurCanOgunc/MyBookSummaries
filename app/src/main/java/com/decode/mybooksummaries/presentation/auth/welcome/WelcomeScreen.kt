package com.decode.mybooksummaries.presentation.auth.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.EmailButtonColor
import com.decode.mybooksummaries.core.ui.theme.HomeBackgroundColor2
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeContract.UiAction
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeContract.UiEffect
import com.decode.mybooksummaries.presentation.auth.welcome.WelcomeContract.UiState
import com.decode.mybooksummaries.presentation.auth.welcome.component.CustomButton
import kotlinx.coroutines.flow.Flow

@Composable
fun WelcomeScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToLogin: () -> Unit
) {

    uiEffect.CollectWithLifecycle {
        when (it) {
            UiEffect.NavigateHome -> navigateToHome()
            UiEffect.NavigateLogin -> navigateToLogin()
            UiEffect.NavigateSignUp -> navigateToSignUp()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(HomeBackgroundColor2)) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoHeader()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.manage_books_summaries_quotes),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(35.dp))

            LoginButtons(onAction)

            Spacer(modifier = Modifier.height(12.dp))

            SignUpFooter { onAction(UiAction.OnSignUpClick) }
        }
    }
}

@Composable
fun LogoHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.booknest),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun LoginButtons(onAction: (UiAction) -> Unit) {
    CustomButton(
        painter = painterResource(id = R.drawable.google),
        title = stringResource(R.string.sign_in_with_google),
        backgroundColor = Color(0xFF4CAF50),
        onClick = { onAction(UiAction.OnLoginWithGoogleClick) }
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Black)
        Text(
            text = stringResource(R.string.or),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Black)
    }

    Spacer(modifier = Modifier.height(12.dp))

    CustomButton(
        painter = painterResource(id = R.drawable.email),
        title = stringResource(R.string.sign_in_with_email),
        backgroundColor = EmailButtonColor,
        onClick = { onAction(UiAction.OnLoginClick) }
    )
}

@Composable
fun SignUpFooter(onSignUpClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_account),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.sign_up),
            color = Color(0xFF4CAF50),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onSignUpClick() }
        )
    }
}