package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.BallColors

/**
 * The drawn numbers of one lottery in a pill-shaped row: the main [numbers] followed by the
 * [bonusNumbers] (Superzahl or Eurozahlen) rendered with [bonusStyle]. Wraps onto a second line
 * when the balls don't fit (large font scale, narrow screens).
 *
 * @param bonusLabel localized name of the bonus numbers (e.g. "Superzahl"), announced by screen readers.
 */
@Composable
fun DrawnNumbers(
    numbers: List<Int>,
    bonusNumbers: List<Int>,
    bonusStyle: BallStyle,
    modifier: Modifier = Modifier,
    bonusLabel: String? = null,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(BallColors.RowBackground, RoundedCornerShape(20.dp))
            .padding(2.dp),
    ) {
        numbers.forEach { LotteryBall(number = it) }
        bonusNumbers.forEach {
            LotteryBall(
                number = it,
                style = bonusStyle,
                contentDescription = bonusLabel?.let { label -> "$label $it" },
            )
        }
    }
}
