package com.decode.mybooksummaries.presentation.edit_profile.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun ProfileInfoCard(email: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.charcoalBlack),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = CustomTheme.colors.softWhite)
            Spacer(modifier = Modifier.width(8.dp))
            Text(email, color = CustomTheme.colors.softWhite, style = CustomTheme.typography.bodyMedium)
        }
    }
}