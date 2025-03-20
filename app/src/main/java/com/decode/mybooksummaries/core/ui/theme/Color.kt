package com.decode.mybooksummaries.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val deepBlue = Color(0xFF1E3A8A)
val electricOrange =  Color(0xFF312F2F)
val darElectricOrange = Color(0xFF3A3A3A)
val softWhite = Color(0xFFF8FAFC)
val darkSoftWhite = Color(0xFFD1D5DB)
val coolGray = Color(0xFF737679)
val darkCoolGray = Color(0xFF8D99AE)
val charcoalBlack = Color(0xFF0F172A)
val slateGray = Color(0xFF404952)
val backgroundColor = Color(0xFFDED7C5)
val darkBackgroundColor = Color(0xFF1E1E1E)
val errorColor = Color(0xFFEF4444)
val darkErrorColor = Color(0xFFFF6B6B)
val textBlack = Color(0xFF232323)
val darkTextBlack = Color(0xFFF8FAFC)
val textWhite = Color(0xFFFFFFFF)
val darkTextWhite = Color(0xFFB0B3B8)
val deepRose = Color(0xFFC71585)
val filterChipColor = Color(0xFF5A3E2B)
val cinnamonRed = Color(0xFFB85C38)
val darkFilterChipColor = Color(0xFF023047)
val neonGreen = Color(0xFF04A777)
val mutedGray = Color(0xFF6D6D6D)
val onyxBlack = Color(0xFF121212)


val LocalLightColors = staticCompositionLocalOf { LightCustomColors }
val LocalDarkColors = staticCompositionLocalOf { DarkCustomColors }

@Immutable
data class CustomColors(
    val deepBlue: Color,
    val electricOrange: Color,
    val softWhite: Color,
    val coolGray: Color,
    val charcoalBlack: Color,
    val slateGray: Color,
    val backgroundColor: Color,
    val errorColor: Color,
    val textBlack: Color,
    val textWhite: Color,
    val deepRose: Color,
    val textTitle: Color,
    val textDesp: Color,
    val filterChipColor: Color,
    val selectedFilterChipColor: Color,
)

val LightCustomColors = CustomColors(
    deepBlue = deepBlue,
    electricOrange = electricOrange,
    softWhite = softWhite,
    coolGray = coolGray,
    charcoalBlack = charcoalBlack,
    slateGray = slateGray,
    backgroundColor = backgroundColor,
    errorColor = errorColor,
    textBlack = textBlack,
    textWhite = textWhite,
    deepRose = deepRose,
    textTitle = Color.Black,
    textDesp = Color.Gray,
    filterChipColor = filterChipColor,
    selectedFilterChipColor = cinnamonRed
)

val DarkCustomColors = CustomColors(
    deepBlue = deepBlue,
    electricOrange = darElectricOrange,
    softWhite = darkSoftWhite,
    coolGray = darkCoolGray,
    charcoalBlack = onyxBlack,
    slateGray = mutedGray,
    backgroundColor = darkBackgroundColor,
    errorColor = darkErrorColor,
    textBlack = darkTextBlack,
    textWhite = darkTextWhite,
    deepRose = deepRose,
    textTitle = Color.White,
    textDesp = textBlack,
    filterChipColor = darkFilterChipColor,
    selectedFilterChipColor = neonGreen
)
