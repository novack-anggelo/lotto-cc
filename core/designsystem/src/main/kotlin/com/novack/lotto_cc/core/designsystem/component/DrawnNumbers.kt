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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.MetalBrushes
import com.novack.lotto_cc.core.designsystem.theme.Spacing

/**
 * Half the height of a one-line row, so it reads as a pill; a fixed radius (not 50%) keeps a wrapped,
 * multi-line row a rounded rectangle instead of a stadium that cuts into the corner balls.
 */
private val RowCornerRadius = 20.dp

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
            .padding(Spacing.xxs),
    ) {
        numbers.forEach { LotteryBall(number = it) }
        if (bonusNumbers.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .testTag(BonusNumbersTestTag)
                    .background(MetalBrushes.Gunmetal, RoundedCornerShape(percent = 50))
                    .padding(Spacing.xxs),
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
