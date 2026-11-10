package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB3261E),
    onPrimary = Color.White,
    background = Color(0xFFF7F7F7),
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
)

/**
 * App theme. No dynamic (wallpaper) colours: the lottery cards carry fixed brand accents
 * ([LotteryColors]) that must stay recognisable.
 */
@Composable
fun LottoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
