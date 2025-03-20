package com.decode.mybooksummaries.presentation.onboarding.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun ButtonUI(
    text: String = stringResource(R.string.next),
    backgroundColor: Color = CustomTheme.colors.charcoalBlack,
    textStyle: TextStyle = CustomTheme.typography.titleSmall,
    textColor: Color = CustomTheme.colors.textBlack,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(10.dp)
    ) {

        Text(text = text, style = textStyle)
    }
}