package com.decode.mybooksummaries.presentation.home.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.SearchBarContainerColor

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    updateSearchQuery: (String) -> Unit,
    onSearch: () -> Unit,
    clearSearchQuery: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = searchText,
        shape = RoundedCornerShape(12.dp),
        onValueChange = updateSearchQuery,
        placeholder = { Text(stringResource(R.string.search_books)) },
        modifier = modifier
            .fillMaxWidth(),
        leadingIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                onSearch()
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            }
        ),
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                val offset by animateDpAsState(
                    if (searchText.isNotEmpty()) 0.dp else (80).dp,
                    label = ""
                )
                IconButton(onClick = {
                    clearSearchQuery()
                }) {
                    Icon(
                        modifier = Modifier.absoluteOffset {
                            IntOffset(
                                x = offset.value.toInt(),
                                y = 0
                            )
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_icon)
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SearchBarContainerColor,
            unfocusedContainerColor = SearchBarContainerColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.LightGray,
            focusedLeadingIconColor = Color.LightGray,
            focusedTrailingIconColor = Color.LightGray,
            unfocusedLeadingIconColor = Color.LightGray,
            unfocusedTrailingIconColor = Color.LightGray,
            focusedPlaceholderColor = Color.LightGray,
            unfocusedPlaceholderColor = Color.LightGray

        ),
    )
}
