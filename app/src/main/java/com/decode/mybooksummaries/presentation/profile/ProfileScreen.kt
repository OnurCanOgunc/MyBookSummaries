package com.decode.mybooksummaries.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decode.mybooksummaries.presentation.profile.component.AchievementIcons
import com.decode.mybooksummaries.presentation.profile.component.ProgressCard
import com.decode.mybooksummaries.presentation.profile.component.ReadingStatistics
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.SearchBarContainerColor
import com.decode.mybooksummaries.presentation.profile.component.ProfileImage

@Composable
fun ProfileScreen(
    uiState: ProfileContract.UiState,
    uiEffect: Flow<ProfileContract.UiEffect>,
    onAction: (ProfileContract.UiAction) -> Unit,
    onNavigateWelcome: () -> Unit,
    onEditProfileClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var selectedImage by remember { mutableStateOf<ProfileImageState>(ProfileImageState.Default) }

    uiEffect.CollectWithLifecycle {
        when (it) {
            ProfileContract.UiEffect.NavigateToWelcome -> {
                onNavigateWelcome()
            }

            ProfileContract.UiEffect.NavigateEditProfile -> {
                onEditProfileClick()
            }

            ProfileContract.UiEffect.NavigateBack ->  {
                onBackClick()
            }
        }
    }

    Scaffold(
        topBar = {
            IconButton(onClick = {onBackClick()},modifier = Modifier.padding(top = 34.dp,start = 16.dp)) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back",tint = Color.White)
            }
        },
        bottomBar = {
            Button(
                onClick = { onAction(ProfileContract.UiAction.LogOut) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SearchBarContainerColor)
            ) {
                Text(text = "Sign Out", color = Color.White)
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E2E))
                .padding(contentPadding)
        ) {
            ProfileHeader(
                email = uiState.email,
                name = uiState.username,
                onEditProfileClick = { onAction(ProfileContract.UiAction.NavigateEditProfile) },
                profileImage = selectedImage,
                onProfilImageChange = { selectedImage = it }
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
    onProfilImageChange: (ProfileImageState) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(
            profileImage = profileImage,
            onImageSelected = onProfilImageChange,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = email,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {onEditProfileClick()},
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(containerColor = SearchBarContainerColor)
        ) {
            Text(text = "Edit Profile")
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
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReadingStatistics(
            booksRead = booksRead,
            booksReadThisMonth = booksReadThisMonth
        )
        Spacer(modifier = Modifier.height(5.dp))
        ProgressCard(booksRead = booksRead,monthlyGoal= monthlyGoal )
        Spacer(modifier = Modifier.height(5.dp))
        AchievementIcons(
            icons = listOf(
                Icons.Default.EmojiEvents,
                Icons.AutoMirrored.Default.MenuBook,
                Icons.Default.Star
            )
        )
    }
}