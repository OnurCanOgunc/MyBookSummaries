package com.decode.mybooksummaries.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import okhttp3.internal.immutableListOf

@Composable
fun AchievementIcons(modifier: Modifier = Modifier) {
    val icons = remember {
        immutableListOf(
            Icons.Default.EmojiEvents,
            Icons.AutoMirrored.Default.MenuBook,
            Icons.Default.Star
        )
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.recent_achievements),
            style = CustomTheme.typography.titleLarge,
            color = CustomTheme.colors.textBlack,
        )
        Spacer(modifier= Modifier.height(10.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            icons.forEach { icon ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CustomTheme.colors.charcoalBlack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = stringResource(R.string.achievement), tint = CustomTheme.colors.softWhite)
                }
            }
        }
    }
}