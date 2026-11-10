package com.novack.lotto_cc.feature.drawresults.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.usecase.GetDrawResultsUseCase
import com.novack.lotto_cc.feature.drawresults.presentation.mapper.toErrorReason
import com.novack.lotto_cc.feature.drawresults.presentation.mapper.toUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Loads the draw results once when created and exposes them as [uiState]. */
class DrawResultsViewModel(
    private val getDrawResults: GetDrawResultsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DrawResultsUiState>(DrawResultsUiState.Loading)
    val uiState: StateFlow<DrawResultsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    /** Loads again after an error. Ignored in any other state, so it never drops shown content. */
    fun retry() {
        if (_uiState.value is DrawResultsUiState.Error) load()
    }

    private fun load() {
        _uiState.value = DrawResultsUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = getDrawResults()) {
                is DomainResult.Success -> DrawResultsUiState.Content(result.data.map { it.toUiModel() })
                is DomainResult.Failure -> DrawResultsUiState.Error(result.error.toErrorReason())
            }
        }
    }
}
