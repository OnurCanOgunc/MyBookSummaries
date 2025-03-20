package com.decode.mybooksummaries.presentation.addbook.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun ButtonGroup(
    isEditMode: Boolean,
    onCancel: () -> Unit,
    onAddBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.errorColor.copy(alpha = 0.8f),
                contentColor = CustomTheme.colors.softWhite
            )
        ) {
            Text(text = stringResource(R.string.cancel), style = CustomTheme.typography.labelLarge)
        }
        Button(
            onClick = onAddBook,
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.electricOrange,
                contentColor = CustomTheme.colors.softWhite
            )
        ) {
            Text(text = if (isEditMode) stringResource(R.string.save) else stringResource(R.string.add_book))
        }
    }
}