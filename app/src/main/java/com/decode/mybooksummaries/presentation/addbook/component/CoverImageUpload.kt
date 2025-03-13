package com.decode.mybooksummaries.presentation.addbook.component

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.decode.mybooksummaries.R
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter

@Composable
fun CoverImageUpload(
    imageUri: Bitmap?,
    onImageSelected: (Uri) -> Unit
) {
    val painter = if (imageUri != null) {
        BitmapPainter(image = imageUri.asImageBitmap())
    } else {
        painterResource(id = R.drawable.img)
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {uri: Uri?->
        uri?.let { onImageSelected(it) }
    }

    Image(
        painter = painter,
        contentDescription = "Book Cover",
        modifier = Modifier.height(180.dp)
            .width(160.dp)
            .border(1.dp, Color.Yellow, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                launcher.launch("image/*")
            },
        contentScale = ContentScale.FillBounds
    )
}

