package com.decode.mybooksummaries.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    searchText: String,
    updateSearchQuery: (String) -> Unit,
    onProfileClick: () -> Unit,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    clearSearchQuery: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp,  bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.booksum),
                style = CustomTheme.typography.headlineMedium,
                color = CustomTheme.colors.textBlack,
                modifier = Modifier.padding(start = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_book),
                        tint = CustomTheme.colors.textBlack,
                        modifier = Modifier.alpha(0.85f)
                    )
                }

                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = stringResource(R.string.profile),
                        tint = CustomTheme.colors.textBlack,
                        modifier = Modifier.alpha(0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SearchBar(
            searchText = searchText,
            updateSearchQuery = updateSearchQuery,
            onSearch = onSearchClick,
            clearSearchQuery = clearSearchQuery
        )
    }
}