package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Brand colours of one lottery card: the side strip with the jackpot, and the "next draw" badge.
 * Every pair meets WCAG AA text contrast (>= 4.5:1).
 */
@Immutable
data class LotteryColors(
    val accent: Color,
    val onAccent: Color,
    val badge: Color,
    val onBadge: Color,
) {
    /** [accent] as a slanted metal sheen that keeps [onAccent] readable, for the jackpot strip. */
    val accentSheen: Brush = slantedMetalSheen(accent, onAccent)

    companion object {
        val Lotto6aus49 = LotteryColors(
            accent = Color(0xFFFFD500),
            onAccent = Color(0xFFB00018), // 5.2:1
            badge = Color(0xFFFFD500),
            onBadge = Color(0xFFB00018),
        )

        val Eurojackpot = LotteryColors(
            accent = Color(0xFF866418),
            onAccent = Color(0xFFFFF3C4), // 4.9:1
            badge = Color(0xFF2E0F04),
            onBadge = Color(0xFFF9D24A), // 12.1:1
        )
    }
}

/** Ball colours are the same in light and dark theme, like real lottery balls. */
internal object BallColors {
    val RegularBackground = Color(0xFFF5F5F5)
    val Content = Color(0xFF212121)
}
