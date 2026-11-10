package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.BallColors
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

/** How a drawn number is rendered. Superzahl and Eurozahlen must look different from regular numbers. */
enum class BallStyle {
    /** Main numbers of any lottery. */
    Regular,

    /** The single LOTTO 6aus49 Superzahl: outlined ball. */
    Superzahl,

    /** The two Eurojackpot Eurozahlen: darker, bordered ball. */
    Eurozahl,
}

/** Exposes a ball's [BallStyle] to UI tests. */
val BallStyleKey = SemanticsPropertyKey<BallStyle>("BallStyle")
private var SemanticsPropertyReceiver.ballStyle by BallStyleKey

private val MinBallSize = 32.dp

/**
 * A drawn number in a circle. [contentDescription] replaces the spoken number, e.g. "Superzahl 7",
 * so screen readers can tell bonus numbers apart.
 */
@Composable
fun LotteryBall(
    number: Int,
    modifier: Modifier = Modifier,
    style: BallStyle = BallStyle.Regular,
    contentDescription: String? = null,
) {
    val background = when (style) {
        BallStyle.Regular -> BallColors.RegularBackground
        BallStyle.Superzahl -> BallColors.SuperzahlBackground
        BallStyle.Eurozahl -> BallColors.EurozahlBackground
    }
    val border = when (style) {
        BallStyle.Regular -> Modifier
        BallStyle.Superzahl -> Modifier.border(2.dp, BallColors.Border, CircleShape)
        BallStyle.Eurozahl -> Modifier.border(1.dp, BallColors.Border, CircleShape)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                ballStyle = style
                contentDescription?.let { this.contentDescription = it }
            }
            .circle(MinBallSize)
            .background(background, CircleShape)
            .then(border)
            .padding(4.dp),
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = BallColors.Content,
        )
    }
}

/**
 * Sizes the element as a square: the larger of its content's width and height, at least [minSize].
 * Grows with the font scale instead of clipping the number, without filling the parent.
 */
private fun Modifier.circle(minSize: Dp): Modifier = layout { measurable, constraints ->
    val contentHeight = measurable.maxIntrinsicHeight(Constraints.Infinity)
    val contentWidth = measurable.maxIntrinsicWidth(contentHeight)
    val side = maxOf(contentWidth, contentHeight, minSize.roundToPx())
        .coerceAtMost(minOf(constraints.maxWidth, constraints.maxHeight))
    val placeable = measurable.measure(Constraints.fixed(side, side))
    layout(side, side) { placeable.place(0, 0) }
}

@Preview
@Composable
private fun LotteryBallStylesPreview() {
    LottoTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BallStyle.entries.forEach { LotteryBall(number = 42, style = it) }
        }
    }
}
