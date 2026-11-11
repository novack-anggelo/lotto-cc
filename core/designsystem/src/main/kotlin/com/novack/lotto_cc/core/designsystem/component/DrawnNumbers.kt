package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import com.novack.lotto_cc.core.designsystem.theme.MetalBrushes
import com.novack.lotto_cc.core.designsystem.theme.Spacing

/**
 * Half the height of a one-line row: a minimum-size ball plus the bonus group's and the row's
 * padding. Shared by the row and the bonus group's background, so their ends line up. A fixed radius
 * (not 50%) keeps larger or wrapped rows rounded rectangles.
 */
private val RowCornerRadius = MinBallSize / 2 + Spacing.xxs + Spacing.xxs

/** Tags the bonus-number group, so UI tests can tell bonus balls from main ones. */
internal const val BonusNumbersTestTag = "DrawnNumbers.bonus"

/**
 * The drawn numbers of one lottery in a pill-shaped row: the main [numbers] followed by the
 * [bonusNumbers] (Superzahl or Eurozahlen) in their own darker group. All balls look the same; the
 * group sets the bonus numbers apart. Wraps onto a second line when the balls don't fit (large font
 * scale, narrow screens), keeping the bonus group together.
 *
 * @param bonusLabel localized name of the bonus numbers (e.g. "Superzahl"), announced by screen readers.
 */
@Composable
fun DrawnNumbers(
    numbers: List<Int>,
    bonusNumbers: List<Int>,
    modifier: Modifier = Modifier,
    bonusLabel: String? = null,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        itemVerticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MetalBrushes.Silver, RoundedCornerShape(RowCornerRadius))
            // Main balls end up Spacing.xs from the edge on every side (vertically via centring).
            .padding(horizontal = Spacing.xs, vertical = Spacing.xxs),
    ) {
        numbers.forEach { LotteryBall(number = it) }
        if (bonusNumbers.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .testTag(BonusNumbersTestTag)
                    .drawBehind {
                        drawBonusBackground(horizontalBleed = Spacing.xs.toPx(), verticalBleed = Spacing.xxs.toPx())
                    }
                    // With the bleed, bonus balls end up Spacing.xs from the dark edge on every side.
                    .padding(start = Spacing.xs, top = Spacing.xxs, bottom = Spacing.xxs),
            ) {
                bonusNumbers.forEach {
                    LotteryBall(
                        number = it,
                        contentDescription = bonusLabel?.let { label -> "$label $it" },
                    )
                }
            }
        }
    }
}

/**
 * The bonus group's background, extended by the row's padding ([horizontalBleed] on the end,
 * [verticalBleed] on the top and bottom), so it reaches the row's edges instead of being inset like
 * the main balls.
 */
private fun DrawScope.drawBonusBackground(horizontalBleed: Float, verticalBleed: Float) {
    val left = if (layoutDirection == LayoutDirection.Ltr) 0f else -horizontalBleed
    val backgroundSize = Size(size.width + horizontalBleed, size.height + 2 * verticalBleed)
    translate(left = left, top = -verticalBleed) {
        val outline = RoundedCornerShape(RowCornerRadius).createOutline(backgroundSize, layoutDirection, this)
        drawOutline(outline, MetalBrushes.Gunmetal)
    }
}
