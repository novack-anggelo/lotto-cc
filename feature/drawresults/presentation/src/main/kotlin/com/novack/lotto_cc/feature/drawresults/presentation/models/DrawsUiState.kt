package com.novack.lotto_cc.feature.drawresults.presentation.models

import com.novack.lotto_cc.core.designsystem.theme.LottoCardUiModel

sealed interface DrawsUiState {
    data class Error(val reason: Errors) : DrawsUiState
    data object Loading : DrawsUiState
    data class Loaded(
        val draws: List<LottoCardUiModel>,
        val refreshing: Boolean = false,
    )
}

sealed interface Errors {
    data object NoDraws : Errors
    data object Others : Errors
}