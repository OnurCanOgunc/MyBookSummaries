package com.decode.mybooksummaries.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.onboarding.components.ButtonUI
import com.decode.mybooksummaries.presentation.onboarding.components.IndicatorUI
import com.decode.mybooksummaries.presentation.onboarding.components.OnboardingGraphUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = remember {
        mutableListOf(
            OnboardingModel.FirstPage,
            OnboardingModel.SecondPage,
            OnboardingModel.ThirdPage,
            OnboardingModel.FourthPage
        )
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val buttonState = remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> mutableListOf("", "Next")
                1 -> mutableListOf("Back", "Next")
                2 -> mutableListOf("Back", "Next")
                3 -> mutableListOf("Back", "Start")
                else -> mutableListOf("", "")
            }
        }
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomOnboarding(
                buttonState = buttonState,
                scope = scope,
                pagerState = pagerState,
                pages = pages,
                onFinished = onFinished
            )
        },
        containerColor = CustomTheme.colors.backgroundColor) {
        Column(Modifier.fillMaxSize().background(CustomTheme.colors.backgroundColor).padding(it)) {
            HorizontalPager(state = pagerState) { index ->
                key(pages[index].title) {
                    OnboardingGraphUI(onboardingModel = pages[index])
                }
            }
        }
    }
}

@Composable
fun BottomOnboarding(
    buttonState: State<List<String>>,
    scope: CoroutineScope,
    pagerState: PagerState,
    pages: List<OnboardingModel>,
    onFinished: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (buttonState.value[0].isNotEmpty()) {
                ButtonUI(
                    text = buttonState.value[0],
                    backgroundColor = Color.Transparent,
                    textStyle = CustomTheme.typography.titleSmall,
                ) {
                    scope.launch {
                        if (pagerState.currentPage > 0) {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IndicatorUI(pageSize = pages.size, currentPage = pagerState.currentPage)
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            ButtonUI(
                text = buttonState.value[1],
                textColor = CustomTheme.colors.softWhite
            ) {
                scope.launch {
                    if (pagerState.currentPage < pages.size - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onFinished()
                    }
                }
            }
        }

    }
}