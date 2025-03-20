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
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun ReadingStatusSelector(selectedStatus: String, onStatusSelected: (String) -> Unit) {
    val statuses = listOf("Queued", "Reading", "Finished")
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statuses.forEach { status ->
            AssistChip(
                modifier = Modifier.width(112.dp),
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        status,
                        style = CustomTheme.typography.labelLarge,
                        color = CustomTheme.colors.softWhite
                    )
                },
                leadingIcon = if (selectedStatus == status) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.width(AssistChipDefaults.IconSize)
                        )
                    }
                } else null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = CustomTheme.colors.filterChipColor,
                    labelColor = CustomTheme.colors.softWhite,
                    leadingIconContentColor = CustomTheme.colors.softWhite,
                    trailingIconContentColor = CustomTheme.colors.softWhite,
                ),
            )
        }
    }
}