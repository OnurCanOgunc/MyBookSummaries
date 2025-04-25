package com.decode.mybooksummaries.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CustomTheme.colors.charcoalBlack,
        unfocusedBorderColor = CustomTheme.colors.slateGray,
        focusedTextColor = CustomTheme.colors.charcoalBlack,
        unfocusedTextColor = CustomTheme.colors.coolGray,
        cursorColor = CustomTheme.colors.charcoalBlack,
        focusedLabelColor = CustomTheme.colors.textBlack,
        unfocusedLabelColor = CustomTheme.colors.coolGray,
        unfocusedLeadingIconColor = CustomTheme.colors.coolGray,
        unfocusedTrailingIconColor = CustomTheme.colors.coolGray,
    )
) {
    var passwordVisibility by remember {
        mutableStateOf(true)
    }
    OutlinedTextField(
        value = value,
        maxLines = 1,
        singleLine = true,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(10.dp),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = icon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = "",
                    tint = CustomTheme.colors.textBlack
                )
            }
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(R.string.toggle_password_visibility),
                        tint = CustomTheme.colors.textBlack,
                    )
                }
            }
        } else null,
        modifier = modifier
            .fillMaxWidth(),
        colors = colors
    )
}