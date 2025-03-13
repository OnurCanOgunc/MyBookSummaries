package com.decode.mybooksummaries.presentation.home.component


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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.decode.mybooksummaries.core.ui.extensions.base64ToBitmap
import androidx.compose.runtime.mutableStateOf
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.HomeTextColor
import com.decode.mybooksummaries.core.ui.theme.SearchBarContainerColor


@Composable
fun BookItem(
    title: String,
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
    Column(
        modifier = Modifier
            .width(100.dp)
            .background(color = SearchBarContainerColor, shape = RoundedCornerShape(8.dp))
            .padding(10.dp)
            .clickable { onItemClick() },
    ) {
        AsyncImage(
            model = bitmap.value?: R.drawable.img,
            contentDescription = "Kitap Resmi",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
        Text(
            text = author,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = HomeTextColor
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = status,
                    fontSize = 11.sp,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray,
                    trackColor = Color.Black,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
        }
    }
}
