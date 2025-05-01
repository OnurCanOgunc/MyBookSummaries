package com.decode.mybooksummaries.presentation.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun MinimalDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onMoveCartClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
    ) {
        IconButton(onClick = onMoveCartClick) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more_options),
                tint = CustomTheme.colors.textBlack
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(R.string.edit),
                        style = CustomTheme.typography.bodyLarge,
                        color = CustomTheme.colors.charcoalBlack
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
                        modifier = Modifier.size(20.dp),
                        tint = CustomTheme.colors.charcoalBlack
                    )
                },
                onClick = onEditClick,
            )
            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(R.string.delete_book),
                        style = CustomTheme.typography.bodyLarge,
                        color = CustomTheme.colors.charcoalBlack
                    )
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        modifier = Modifier.size(20.dp),
                        tint = CustomTheme.colors.charcoalBlack
                    )
                },
                onClick = onDeleteClick
            )

        }
    }
}