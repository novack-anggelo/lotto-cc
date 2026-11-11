package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.core.designsystem.theme.Spacing
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel

/** Both lotteries, and a card whose next jackpot is unknown. */
internal class DrawCardProvider : CollectionPreviewParameterProvider<DrawCardUiModel>(
    listOf(PreviewData.lotto6aus49, PreviewData.eurojackpot, PreviewData.eurojackpot.copy(jackpot = null)),
)

@Preview(showBackground = true, backgroundColor = 0xFFF7F7F7)
@Preview(showBackground = true, backgroundColor = 0xFFF7F7F7, locale = "de")
@Composable
private fun DrawCardPreview(@PreviewParameter(DrawCardProvider::class) card: DrawCardUiModel) {
    LottoTheme { DrawCard(card, onClick = {}, Modifier.padding(Spacing.l)) }
}
