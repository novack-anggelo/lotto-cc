package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

internal class DrawnNumbersSample(val numbers: List<Int>, val bonusNumbers: List<Int>)

/** LOTTO 6aus49 (one Superzahl), Eurojackpot (two Eurozahlen) and a draw without bonus numbers. */
internal class DrawnNumbersProvider : CollectionPreviewParameterProvider<DrawnNumbersSample>(
    listOf(
        DrawnNumbersSample(listOf(1, 2, 3, 14, 25, 36), listOf(4)),
        DrawnNumbersSample(listOf(1, 2, 3, 14, 25), listOf(36, 42)),
        DrawnNumbersSample(listOf(7, 3, 1, 2, 2, 4, 4), emptyList()),
    ),
)

@Preview
@Composable
private fun DrawnNumbersPreview(@PreviewParameter(DrawnNumbersProvider::class) sample: DrawnNumbersSample) {
    LottoTheme { DrawnNumbers(sample.numbers, sample.bonusNumbers) }
}
