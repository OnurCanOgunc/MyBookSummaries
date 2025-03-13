package com.decode.mybooksummaries.presentation.addbook.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.theme.LinearProgressIndicatorColor
import com.decode.mybooksummaries.core.ui.theme.LinearProgressIndicatorTrackColor

@Composable
fun ReadingStatusSelector(selectedStatus: String, onStatusSelected: (String) -> Unit) {
    val statuses = listOf("To Read", "Reading", "Read")
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statuses.forEach { status ->
            AssistChip(
                modifier = Modifier.width(110.dp),
                onClick = { onStatusSelected(status) },
                label = { Text(status, color = Color.White) },
                leadingIcon = if (selectedStatus == status) {
                    { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
                } else null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedStatus == status) LinearProgressIndicatorColor else LinearProgressIndicatorTrackColor,
                ),
            )
        }
    }
}