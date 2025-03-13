package com.decode.mybooksummaries.presentation.home.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import okhttp3.internal.immutableListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.decode.mybooksummaries.core.ui.theme.SearchBarContainerColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(onClick: () -> Unit) {
    var checkedItem by rememberSaveable { mutableIntStateOf(0) }
    val options = immutableListOf<String>(
        "Hepsi", "Kişisel Gelişim", "Tarih",
        "Roman", "Bilim Kurgu", "Macera",
    )
    val scrollState = rememberScrollState()

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)

    ) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                onClick = {
                    checkedItem = index
                },
                selected = checkedItem == index,
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.count(),
                ),
                modifier = Modifier,
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = SearchBarContainerColor,
                    activeContentColor = Color.White,
                    activeBorderColor = Color.Transparent,

                )
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Book",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = option,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}