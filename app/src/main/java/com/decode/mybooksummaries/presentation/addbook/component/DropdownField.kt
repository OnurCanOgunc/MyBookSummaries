package com.decode.mybooksummaries.presentation.addbook.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import okhttp3.internal.immutableListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, selectedItem: String, onItemSelected: (String) -> Unit) {
    val items = immutableListOf(
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
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = CustomTheme.typography.labelLarge,
            color = CustomTheme.colors.textBlack,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(48.dp),
                textStyle = TextStyle(textAlign = TextAlign.Start,color = CustomTheme.colors.textBlack),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = CustomTheme.colors.textBlack,
                    unfocusedTextColor = CustomTheme.colors.textBlack,
                    focusedContainerColor = CustomTheme.colors.backgroundColor,
                    unfocusedContainerColor = CustomTheme.colors.backgroundColor,
                    focusedIndicatorColor = CustomTheme.colors.coolGray,
                    focusedTrailingIconColor = CustomTheme.colors.textBlack,
                    unfocusedTrailingIconColor = CustomTheme.colors.textBlack
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item,
                            color = CustomTheme.colors.textBlack) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}