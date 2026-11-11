package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason

private val previewCards = listOf(PreviewData.lotto6aus49, PreviewData.eurojackpot.copy(jackpot = null))

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
@Composable
private fun DrawResultsContentPreview() {
    LottoTheme { DrawResultsScreen(DrawResultsUiState.Content(previewCards), onRetry = {}) }
}

@Preview(showBackground = true)
@Composable
private fun DrawResultsErrorPreview() {
    LottoTheme { DrawResultsScreen(DrawResultsUiState.Error(ErrorReason.NoConnection), onRetry = {}) }
}
