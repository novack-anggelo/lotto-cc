package com.novack.lotto_cc.feature.drawresults.presentation.model

sealed interface DrawResultsUiState {
    data object Loading : DrawResultsUiState

    data class Content(val cards: List<DrawCardUiModel>) : DrawResultsUiState

    data class Error(val reason: ErrorReason) : DrawResultsUiState
}

/** Why loading failed; the screen picks the message (e.g. "check your connection"). */
enum class ErrorReason { NoConnection, Server, Unknown }
