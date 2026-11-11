package com.novack.lotto_cc.core.designsystem.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance

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
 * A slanted brushed-metal sheen in [base]'s tone, like the numbers row, repeating along the element
 * so tall shapes get stripes too. The stripes only move away from [content] (lighter behind dark
 * text, darker behind light text), so text keeps at least the contrast it has on plain [base].
 */
internal fun slantedMetalSheen(base: Color, content: Color): Brush {
    val towards = if (content.luminance() < base.luminance()) Color.White else Color.Black
    fun shade(fraction: Float) = lerp(base, towards, fraction)
    return SlantedStripes(
        listOf(0.00f to base, 0.30f to shade(0.30f), 0.50f to base, 0.75f to shade(0.15f), 1.00f to base),
    )
}

/** A gradient across the element's width, tilted downwards and mirrored to fill any height. */
private class SlantedStripes(private val stops: List<Pair<Float, Color>>) : ShaderBrush() {
    override fun createShader(size: Size): Shader = LinearGradientShader(
        from = Offset.Zero,
        to = Offset(size.width * 1.5f, size.width * 0.75f),
        colors = stops.map { it.second },
        colorStops = stops.map { it.first },
        tileMode = TileMode.Mirror,
    )
}
