package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

@Preview
@Composable
private fun LotteryBallStylesPreview() {
    LottoTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BallStyle.entries.forEach { LotteryBall(number = 42, style = it) }
        }
    }
}
