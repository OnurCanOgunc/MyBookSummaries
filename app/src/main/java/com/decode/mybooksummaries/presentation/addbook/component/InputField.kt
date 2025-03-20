package com.decode.mybooksummaries.presentation.addbook.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions? = null
) {
    Column {
        Text(
            text = label,
            style = CustomTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 4.dp),
            color = CustomTheme.colors.textBlack
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            readOnly = readOnly,
            shape = RoundedCornerShape(6.dp),
            modifier = modifier.fillMaxWidth().height(48.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textBlack,
                unfocusedTextColor = CustomTheme.colors.textBlack,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = CustomTheme.colors.charcoalBlack,
                unfocusedIndicatorColor = CustomTheme.colors.coolGray,
            ),
            textStyle = TextStyle(textAlign = TextAlign.Start,color = CustomTheme.colors.textBlack),
            keyboardOptions = keyboardOptions?: KeyboardOptions.Default
        )
    }
}