package com.decode.mybooksummaries.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun MyBookSummariesTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLightColors provides CustomTheme.colors,
        LocalTypography provides CustomTheme.typography
    ) {
        content()
    }
}

object CustomTheme {
    val colors: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) LocalDarkColors.current else LocalLightColors.current
    val typography: CustomTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}