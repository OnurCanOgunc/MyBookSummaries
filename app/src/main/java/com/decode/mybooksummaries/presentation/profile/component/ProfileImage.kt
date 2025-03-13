package com.decode.mybooksummaries.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.decode.mybooksummaries.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.decode.mybooksummaries.presentation.profile.ProfileImageState

@Composable
fun ProfileImage(
    profileImage: ProfileImageState,
    onImageSelected: (ProfileImageState) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val painter = when (profileImage) {
        is ProfileImageState.UriImage -> rememberAsyncImagePainter(model = profileImage.uri)
        is ProfileImageState.ResourceImage -> painterResource(id = profileImage.resId)
        is ProfileImageState.Default -> painterResource(id = R.drawable.profile_image)
    }


    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .clickable {
                showDialog = true
            },
        contentAlignment = Alignment.BottomEnd
    ) {

        Image(
            painter = painter,
            contentDescription = "Profile Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
    if (showDialog) {
        AvatarSelectionDialog(
            onAvatarSelected = { avatar ->
                onImageSelected(ProfileImageState.ResourceImage(avatar))
                showDialog = false
            },
            onGallerySelected = { uri ->
                uri?.let {
                    onImageSelected(ProfileImageState.UriImage(it))
                }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

