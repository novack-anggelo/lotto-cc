package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason

/** Every screen state. */
internal class DrawResultsUiStateProvider : CollectionPreviewParameterProvider<DrawResultsUiState>(
    listOf(
        DrawResultsUiState.Content(listOf(PreviewData.lotto6aus49, PreviewData.eurojackpot)),
        DrawResultsUiState.Loading,
        DrawResultsUiState.Error(ErrorReason.NoConnection),
    ),
)

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
@Composable
private fun DrawResultsScreenPreview(
    @PreviewParameter(DrawResultsUiStateProvider::class) uiState: DrawResultsUiState,
) {
    LottoTheme { DrawResultsScreen(uiState, onRetry = {}) }
}
