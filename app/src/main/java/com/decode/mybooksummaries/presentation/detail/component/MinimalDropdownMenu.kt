package com.decode.mybooksummaries.presentation.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decode.mybooksummaries.R

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
            .padding(16.dp)
    ) {
        IconButton(onClick = onMoveCartClick ) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more_options), tint = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            DropdownMenuItem(
                text = { Text("Edit", fontSize = 14.sp) },
                trailingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit),modifier = Modifier.size(20.dp))
                },
                onClick = onEditClick,
            )
            DropdownMenuItem(
                text = { Text("Delete Book",fontSize = 14.sp) },
                trailingIcon = {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete), modifier = Modifier.size(20.dp))
                },
                onClick = onDeleteClick
            )
        }
    }
}