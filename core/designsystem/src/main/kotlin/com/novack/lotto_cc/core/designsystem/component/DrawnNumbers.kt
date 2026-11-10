package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.BallColors
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

/**
 * The drawn numbers of one lottery in a pill-shaped row: the main [numbers] followed by the
 * [bonusNumbers] (Superzahl or Eurozahlen) rendered with [bonusStyle].
 */
@Composable
fun DrawnNumbers(
    numbers: List<Int>,
    bonusNumbers: List<Int>,
    bonusStyle: BallStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(BallColors.RowBackground, CircleShape)
            .padding(2.dp),
    ) {
        numbers.forEach { LotteryBall(number = it) }
        bonusNumbers.forEach { LotteryBall(number = it, style = bonusStyle) }
    }
}

@Preview
@Composable
private fun DrawnNumbersLotto6aus49Preview() {
    LottoTheme {
        DrawnNumbers(listOf(1, 2, 3, 14, 25, 36), listOf(4), BallStyle.Superzahl)
    }
}

@Preview
@Composable
private fun DrawnNumbersEurojackpotPreview() {
    LottoTheme {
        DrawnNumbers(listOf(1, 2, 3, 14, 25), listOf(36, 42), BallStyle.Eurozahl)
    }
}
