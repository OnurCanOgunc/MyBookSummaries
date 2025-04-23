package com.decode.mybooksummaries.presentation.onboarding.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.onboarding.OnboardingModel

@Composable
fun OnboardingGraphUI(onboardingModel: OnboardingModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = onboardingModel.image,
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)
                    .padding(10.dp),
            )
            Text(
                text = onboardingModel.title,
                modifier = Modifier.fillMaxWidth(),
                style = CustomTheme.typography.bodyExtraLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = CustomTheme.colors.textBlack
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = onboardingModel.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 0.dp),
                textAlign = TextAlign.Center,
                style = CustomTheme.typography.titleMedium,
                color = CustomTheme.colors.textBlack
            )
        }
    }
}
