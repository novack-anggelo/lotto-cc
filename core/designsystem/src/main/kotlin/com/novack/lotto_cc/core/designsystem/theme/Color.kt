package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** Brand colours of one lottery card: the side strip with the jackpot, and the "next draw" badge. */
@Immutable
data class LotteryColors(
    val accent: Color,
    val onAccent: Color,
    val badge: Color,
    val onBadge: Color,
) {
    companion object {
        val Lotto6aus49 = LotteryColors(
            accent = Color(0xFFFFD500),
            onAccent = Color(0xFFD2001E),
            badge = Color(0xFFFFD500),
            onBadge = Color(0xFFD2001E),
        )

        val Eurojackpot = LotteryColors(
            accent = Color(0xFFA8822E),
            onAccent = Color(0xFFF9D24A),
            badge = Color(0xFF2E0F04),
            onBadge = Color(0xFFF9D24A),
        )
    }
}

/** Ball colours are the same in light and dark theme, like real lottery balls. */
internal object BallColors {
    val RegularBackground = Color(0xFFEEEEEE)
    val SuperzahlBackground = Color(0xFFFFFFFF)
    val EurozahlBackground = Color(0xFFBDBDBD)
    val Border = Color(0xFF616161)
    val Content = Color(0xFF212121)
    val RowBackground = Color(0xFFF5F5F5)
}
