package com.decode.mybooksummaries.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.decode.mybooksummaries.core.ui.extensions.base64ToBitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme


@Composable
fun BookItem(
    author: String,
    status: String,
    coverImage: String,
    pageCount: String,
    currentPage: String,
    onItemClick: () -> Unit
) {
    val bitmap = remember { mutableStateOf(coverImage.base64ToBitmap()) }
    val progress = remember {
        val totalPages = pageCount.toIntOrNull() ?: 1
        val current = currentPage.toIntOrNull() ?: 0
        if (totalPages > 0) {
            (current.toFloat() / totalPages.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.charcoalBlack),
        border = BorderStroke(1.dp, CustomTheme.colors.softWhite)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = bitmap.value ?: R.drawable.img,
                contentDescription = stringResource(R.string.book_image, author),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(8.dp))

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = author,
                style = CustomTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = CustomTheme.colors.textWhite,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = CustomTheme.colors.deepBlue,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = status,
                        style = CustomTheme.typography.labelMedium,
                        color = CustomTheme.colors.textWhite
                    )
                }

                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        color = CustomTheme.colors.deepRose,
                        trackColor = CustomTheme.colors.slateGray,
                        strokeWidth = 3.dp
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = CustomTheme.typography.labelMedium,
                        color = CustomTheme.colors.textWhite,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
