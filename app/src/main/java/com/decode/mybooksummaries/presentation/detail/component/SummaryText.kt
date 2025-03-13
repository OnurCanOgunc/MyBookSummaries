package com.decode.mybooksummaries.presentation.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryText(summary: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (expanded) summary else summary.take(200) + "...",
            fontSize = 16.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TextButton(onClick = { expanded = !expanded }) {
            Text(text = if (expanded) "Daha Az Göster" else "Devamını Oku")
        }
    }
}


