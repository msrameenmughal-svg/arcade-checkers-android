package com.arcade.checkers.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arcade.checkers.R

// Typography extracted from Stitch designTheme.typography:
// Headlines: Space Grotesk | Body: Chivo | Labels: Space Mono
// Available font files in res/font are loaded; system fonts act as fallbacks.

private val spaceMono = FontFamily(
    Font(R.font.space_mono_regular, FontWeight.Normal),
    Font(R.font.space_mono_bold, FontWeight.Bold)
)

val HeadlineFont = FontFamily.SansSerif
val BodyFont = FontFamily.SansSerif
val MonoFont = spaceMono

val ArcadeTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = HeadlineFont, fontSize = 40.sp, fontWeight = FontWeight.Bold,
        lineHeight = 48.sp, letterSpacing = (-0.8).sp
    ),
    displayMedium = TextStyle(
        fontFamily = HeadlineFont, fontSize = 28.sp, fontWeight = FontWeight.Bold,
        lineHeight = 36.sp, letterSpacing = (-0.56).sp
    ),
    displaySmall = TextStyle(
        fontFamily = HeadlineFont, fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = HeadlineFont, fontSize = 18.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = HeadlineFont, fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = HeadlineFont, fontSize = 14.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFont, fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFont, fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = BodyFont, fontSize = 12.sp, fontWeight = FontWeight.Normal, lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = MonoFont, fontSize = 14.sp, fontWeight = FontWeight.Bold, lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MonoFont, fontSize = 12.sp, fontWeight = FontWeight.Bold, lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MonoFont, fontSize = 10.sp, fontWeight = FontWeight.Bold, lineHeight = 14.sp
    )
)
