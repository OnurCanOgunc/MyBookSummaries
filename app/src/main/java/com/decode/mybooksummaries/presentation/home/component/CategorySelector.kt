package com.decode.mybooksummaries.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import okhttp3.internal.immutableListOf

@Composable
fun CategorySelector(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    val items = immutableListOf(
        "All",
        "Fiction",
        "Romance",
        "History",
        "Science Fiction",
        "Biography",
        "Self-help",
        "Horror",
        "Dystopian",
        "Others"
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items, key = { it }) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category, style = CustomTheme.typography.bodyMedium,color = CustomTheme.colors.softWhite) },
                modifier = Modifier.padding(horizontal = 4.dp),
                leadingIcon = if (selectedCategory == category) {
                    {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = stringResource(R.string.selected),
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                            tint = CustomTheme.colors.softWhite
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = CustomTheme.colors.filterChipColor,
                    selectedContainerColor = CustomTheme.colors.selectedFilterChipColor ),
                elevation = FilterChipDefaults.filterChipElevation(4.dp),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = false,
                    selected = false,
                )
            )
        }
    }
}