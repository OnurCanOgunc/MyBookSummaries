package com.decode.mybooksummaries.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme


@Composable
fun ReadingStatistics(
    modifier: Modifier = Modifier,
    booksRead: String,
    booksReadThisMonth: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.reading_statistics),
            style = CustomTheme.typography.titleLarge,
            color = CustomTheme.colors.textBlack,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatisticCard(value = booksRead, title = stringResource(R.string.books_read))
            StatisticCard(value = booksReadThisMonth, title = stringResource(R.string.this_month))
        }

    }
}

@Composable
fun StatisticCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {
    Column(
        modifier = modifier
            .size(width = 180.dp, height = 90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CustomTheme.colors.charcoalBlack)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = value,
            style = CustomTheme.typography.titleLarge,
            color = CustomTheme.colors.softWhite
        )
        Text(
            text = title,
            style = CustomTheme.typography.titleLarge,
            color = CustomTheme.colors.softWhite
        )

    }

}