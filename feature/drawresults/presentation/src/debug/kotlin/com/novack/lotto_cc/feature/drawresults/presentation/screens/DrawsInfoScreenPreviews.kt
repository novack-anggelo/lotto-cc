package com.novack.lotto_cc.feature.drawresults.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.feature.drawresults.presentation.PreviewData
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.models.Errors

private class DrawsUiStateProvider : CollectionPreviewParameterProvider<DrawsUiState>(
    listOf(
        DrawsUiState.Loaded(PreviewData.draws),
        DrawsUiState.Loaded(PreviewData.draws, refreshing = true),
        DrawsUiState.Loading,
        DrawsUiState.Error(Errors.Network),
    ),
)

/** The screen in every state. */
@Preview(showSystemUi = true)
@Composable
private fun DrawsInfoScreenPreview(@PreviewParameter(DrawsUiStateProvider::class) state: DrawsUiState) {
    LottoTheme {
        DrawsInfoScreen(state = state, onRetry = {}, onRefresh = {}, onClick = {})
    }
}
