package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Red brand primary with neutral warm secondary/tertiary, so no Material baseline purple leaks through.
private val LightColors = lightColorScheme(
    primary = Color(0xFFB3261E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),
    secondary = Color(0xFF775652),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDAD6),
    onSecondaryContainer = Color(0xFF2C1512),
    tertiary = Color(0xFF715B2E),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFDDFA6),
    onTertiaryContainer = Color(0xFF261900),
    background = Color(0xFFF7F7F7),
    surface = Color.White,
    // Warm paper neutrals for cards and containers (Material's defaults are lavender).
    surfaceVariant = Color(0xFFEAE3D9),
    onSurfaceVariant = Color(0xFF4F4943),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFFFCF5),
    surfaceContainer = Color(0xFFF8F4EC),
    surfaceContainerHigh = Color(0xFFF2EEE6),
    surfaceContainerHighest = Color(0xFFECE8E0),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    onPrimary = Color(0xFF690005),
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = Color(0xFFE7BDB7),
    onSecondary = Color(0xFF442926),
    secondaryContainer = Color(0xFF5D3F3B),
    onSecondaryContainer = Color(0xFFFFDAD6),
    tertiary = Color(0xFFE0C38C),
    onTertiary = Color(0xFF3F2E04),
    tertiaryContainer = Color(0xFF574419),
    onTertiaryContainer = Color(0xFFFDDFA6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF4D4640),
    onSurfaceVariant = Color(0xFFD1C8BE),
    surfaceContainerLowest = Color(0xFF0F0F0F),
    surfaceContainerLow = Color(0xFF1E1C19),
    surfaceContainer = Color(0xFF23211D),
    surfaceContainerHigh = Color(0xFF2D2B27),
    surfaceContainerHighest = Color(0xFF383632),
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
