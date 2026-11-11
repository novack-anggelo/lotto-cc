package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

/** One- and two-digit numbers: the ball keeps its size and stays round. */
internal class BallNumberProvider : CollectionPreviewParameterProvider<Int>(listOf(7, 42))

@Preview
@Composable
private fun LotteryBallPreview(@PreviewParameter(BallNumberProvider::class) number: Int) {
    LottoTheme { LotteryBall(number = number) }
}
