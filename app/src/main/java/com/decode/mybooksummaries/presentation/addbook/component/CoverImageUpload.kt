package com.decode.mybooksummaries.presentation.addbook.component

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.decode.mybooksummaries.R
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.decode.mybooksummaries.core.ui.extensions.base64ToBitmap
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun CoverImageUpload(
    imageUrl: String,
    onImageSelected: (Uri) -> Unit
) {
    val imageBitmap: Bitmap? by remember(imageUrl) {
        derivedStateOf { imageUrl.base64ToBitmap() }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {uri: Uri?->
        uri?.let { onImageSelected(it) }
    }

    AsyncImage(
        model = imageBitmap ?: R.drawable.img,
        contentDescription = stringResource(R.string.book_cover),
        modifier = Modifier
            .height(180.dp)
            .width(160.dp)
            .border(1.dp, CustomTheme.colors.electricOrange, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                launcher.launch("image/*")
            },
        contentScale = ContentScale.FillBounds
    )
}

