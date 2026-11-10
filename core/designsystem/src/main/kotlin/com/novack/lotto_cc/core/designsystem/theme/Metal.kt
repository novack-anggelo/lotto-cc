package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

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
