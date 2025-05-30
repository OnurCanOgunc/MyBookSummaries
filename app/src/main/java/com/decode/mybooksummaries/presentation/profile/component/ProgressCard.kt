package com.decode.mybooksummaries.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun ProgressCard(
    booksRead: String,
    monthlyGoal: Int,
    modifier: Modifier = Modifier
) {
    val progress = remember {
        val monthlyGoalBook = monthlyGoal
        val current = booksRead.toIntOrNull() ?: 0
        if (monthlyGoalBook > 0) {
            (current.toFloat() / monthlyGoal.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                CustomTheme.colors.charcoalBlack,
                RoundedCornerShape(12.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.monthly_goal),
            style = CustomTheme.typography.bodyExtraLarge,
            fontWeight = FontWeight.SemiBold,
            color = CustomTheme.colors.softWhite,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(50)),
            color = CustomTheme.colors.deepRose,
            trackColor = CustomTheme.colors.slateGray,

            )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$booksRead/$monthlyGoal",
            style = CustomTheme.typography.bodyMedium,
            color = CustomTheme.colors.softWhite,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}