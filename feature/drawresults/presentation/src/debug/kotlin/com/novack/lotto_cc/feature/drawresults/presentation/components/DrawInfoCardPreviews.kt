package com.novack.lotto_cc.feature.drawresults.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.core.designsystem.theme.Spacing
import com.novack.lotto_cc.feature.drawresults.presentation.PreviewData
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawCardUiModel

private class DrawCardProvider : CollectionPreviewParameterProvider<DrawCardUiModel>(
    listOf(PreviewData.lotto6aus49, PreviewData.eurojackpot, PreviewData.withoutJackpot),
)

/** Every sample card, at normal and at large font size. */
@Preview(showBackground = true)
@Preview(showBackground = true, fontScale = 1.5f)
@Composable
private fun DrawInfoCardPreview(@PreviewParameter(DrawCardProvider::class) card: DrawCardUiModel) {
    LottoTheme {
        DrawInfoCard(card, onClick = {}, modifier = Modifier.padding(Spacing.l))
    }
}
