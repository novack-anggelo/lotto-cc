package com.novack.lotto_cc.feature.drawresults.presentation.models


sealed interface DrawsUiState {
    data class Error(val reason: Errors) : DrawsUiState
    data object Loading : DrawsUiState
    data class Loaded(
        val draws: List<DrawCardUiModel>,
        val refreshing: Boolean = false,
    ) : DrawsUiState
}

enum class Errors {
    Network,
    Server,
    Unknown,
}
