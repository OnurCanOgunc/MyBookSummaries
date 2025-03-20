package com.decode.mybooksummaries.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun CustomBackButton(back: () -> Unit) {
    IconButton(
        onClick = back ,
        modifier = Modifier.padding(top = 24.dp, start = 16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(R.string.back),
            tint = CustomTheme.colors.textBlack
        )
    }
}