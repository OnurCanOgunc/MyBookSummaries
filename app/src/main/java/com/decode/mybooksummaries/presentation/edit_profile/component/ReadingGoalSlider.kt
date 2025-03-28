package com.decode.mybooksummaries.presentation.edit_profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileContract.UiAction
import com.decode.mybooksummaries.presentation.edit_profile.EditProfileContract.UiState

@Composable
fun ReadingGoalSlider(uiState: UiState, onAction: (UiAction) -> Unit) {
    Text(
        "Reading Goal",
        style = MaterialTheme.typography.titleMedium.copy(color = CustomTheme.colors.textTitle)
    )
    Spacer(modifier = Modifier.height(8.dp))

    Column {
        Text(
            stringResource(R.string.monthly_book_goal, uiState.monthlyBookGoal),
            color = CustomTheme.colors.textBlack,
            style = CustomTheme.typography.bodyMedium
        )
        Slider(
            value = uiState.monthlyBookGoal.toFloat(),
            onValueChange = { onAction(UiAction.OnMonthlyBookGoalChange(it.toInt())) },
            valueRange = 1f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = CustomTheme.colors.electricOrange,
                activeTrackColor = CustomTheme.colors.electricOrange,
                inactiveTrackColor = CustomTheme.colors.coolGray
            )
        )
    }
}