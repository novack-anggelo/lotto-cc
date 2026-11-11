package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Static metallic sheens: alternating light and dark bands across the element's diagonal, like
 * brushed metal. Highlights stay below the ball colour so the balls don't blend into the row.
 */
internal object MetalBrushes {
    val Silver = Brush.linearGradient(
        0.00f to Color(0xFFC4C4C4),
        0.18f to Color(0xFFE6E6E6),
        0.36f to Color(0xFFCCCCCC),
        0.52f to Color(0xFFB4B4B4),
        0.70f to Color(0xFFE0E0E0),
        0.86f to Color(0xFFC8C8C8),
        1.00f to Color(0xFFB8B8B8),
    )

    /** Darker variant for the bonus-number group, so it still stands out from [Silver]. */
    val Gunmetal = Brush.linearGradient(
        0.00f to Color(0xFF4A4A4A),
        0.25f to Color(0xFF7A7A7A),
        0.45f to Color(0xFF555555),
        0.65f to Color(0xFF3E3E3E),
        0.85f to Color(0xFF6E6E6E),
        1.00f to Color(0xFF444444),
    )
}

/**
 * A vertical brushed-metal sheen in [base]'s tone: light and dark bands from the top, ending in
 * plain [base] over the bottom fifth, so text placed there keeps [base]'s contrast.
 */
internal fun verticalMetalSheen(base: Color): Brush {
    fun lighter(fraction: Float) = lerp(base, Color.White, fraction)
    fun darker(fraction: Float) = lerp(base, Color.Black, fraction)
    return Brush.verticalGradient(
        0.00f to lighter(0.30f),
        0.18f to base,
        0.36f to darker(0.15f),
        0.56f to lighter(0.20f),
        0.80f to base,
        1.00f to base,
    )
}
