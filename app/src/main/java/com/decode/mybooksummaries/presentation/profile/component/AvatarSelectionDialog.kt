package com.decode.mybooksummaries.presentation.profile.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvatarSelectionDialog(
    onAvatarSelected: (Int) -> Unit,
    onGallerySelected: (Uri?) -> Unit,
    onDismiss: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onGallerySelected(uri)
    }

    val avatarResIds = listOf(
        R.drawable.avatar1, R.drawable.avatar3,
        R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
        R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
        R.drawable.avatar10,
    )

    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier) {
            Text(stringResource(R.string.select_profile_picture), style = CustomTheme.typography.titleLarge, color = CustomTheme.colors.softWhite)

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                avatarResIds.forEach { avatar ->
                    Image(
                        painter = painterResource(id = avatar),
                        contentDescription = stringResource(R.string.avatar),
                        modifier = Modifier
                            .padding(4.dp)
                            .size(60.dp)
                            .clip(CircleShape)
                            .clickable { onAvatarSelected(avatar) },
                        contentScale = ContentScale.Fit
                    )
                }
                Button(onClick = { launcher.launch("image/*") }, Modifier.padding(start = 27.dp, top = 16.dp)) {
                    Text(stringResource(R.string.select_from_gallery), color = CustomTheme.colors.softWhite)
                }
            }

        }
    }
}