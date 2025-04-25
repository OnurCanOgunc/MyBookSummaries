package com.decode.mybooksummaries.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.presentation.profile.component.AchievementIcons
import com.decode.mybooksummaries.presentation.profile.component.ProgressCard
import com.decode.mybooksummaries.presentation.profile.component.ReadingStatistics
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.res.stringResource
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.presentation.profile.component.ProfileImage
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun ProfileScreen(
    uiState: ProfileContract.UiState,
    uiEffect: Flow<ProfileContract.UiEffect>,
    onAction: (ProfileContract.UiAction) -> Unit,
    onNavigateWelcome: () -> Unit,
    onEditProfileClick: () -> Unit,
    onBackClick: () -> Unit
) {

    uiEffect.CollectWithLifecycle {
        when (it) {
            ProfileContract.UiEffect.NavigateToWelcome -> {
                onNavigateWelcome()
            }

            ProfileContract.UiEffect.NavigateEditProfile -> {
                onEditProfileClick()
            }

            ProfileContract.UiEffect.NavigateBack -> {
                onBackClick()
            }
        }
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = CustomTheme.colors.textBlack
                )
            }
        },
        bottomBar = {
            Button(
                onClick = { onAction(ProfileContract.UiAction.LogOut) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CustomTheme.colors.charcoalBlack)
            ) {
                Text(text = stringResource(R.string.sign_out), color = CustomTheme.colors.softWhite)
            }
        },
        containerColor = CustomTheme.colors.backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProfileHeader(
                email = uiState.email,
                name = uiState.username,
                onEditProfileClick = { onAction(ProfileContract.UiAction.NavigateEditProfile) },
                profileImage = uiState.profileImage,
                onProfileImageChange = { onAction(ProfileContract.UiAction.ProfileImageSelected(it)) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileStatistics(
                booksRead = uiState.totalBooksRead,
                booksReadThisMonth = uiState.booksReadThisMonth,
                monthlyGoal = uiState.monthlyBookGoal,
            )
        }
    }
}

@Composable
fun ProfileHeader(
    email: String,
    name: String,
    onEditProfileClick: () -> Unit,
    profileImage: ProfileImageState,
    onProfileImageChange: (ProfileImageState) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(
            profileImage = profileImage,
            onImageSelected = onProfileImageChange,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = CustomTheme.typography.bodyExtraLarge,
            color = CustomTheme.colors.textBlack
        )
        Text(
            text = email,
            style = CustomTheme.typography.bodyMedium,
            color = CustomTheme.colors.slateGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onEditProfileClick() },
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(containerColor = CustomTheme.colors.charcoalBlack)
        ) {
            Text(text = stringResource(R.string.edit_profile), color = CustomTheme.colors.softWhite)
        }
    }
}

@Composable
fun ProfileStatistics(
    booksRead: String,
    booksReadThisMonth: String,
    monthlyGoal: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReadingStatistics(
            booksRead = booksRead,
            booksReadThisMonth = booksReadThisMonth
        )
        Spacer(modifier = Modifier.height(5.dp))
        ProgressCard(booksRead = booksRead, monthlyGoal = monthlyGoal)
        Spacer(modifier = Modifier.height(5.dp))
        AchievementIcons()
    }
}