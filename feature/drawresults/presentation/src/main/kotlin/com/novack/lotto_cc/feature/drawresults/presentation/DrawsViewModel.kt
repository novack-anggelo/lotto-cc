package com.novack.lotto_cc.feature.drawresults.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novack.lotto_cc.core.domain.models.DomainError
import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.GetDrawsUseCase
import com.novack.lotto_cc.feature.drawresults.presentation.mappers.toUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.models.Errors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DrawsViewModel(private val useCase: GetDrawsUseCase) : ViewModel() {

    private val _state = MutableStateFlow<DrawsUiState>(DrawsUiState.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    private fun fetch() {
        viewModelScope.launch {
            when (val draws = useCase()) {
                is DomainResult.Failure -> {
                    val error = when (draws.error) {
                        DomainError.NETWORK -> Errors.Network
                        DomainError.SERVER -> Errors.Server
                        DomainError.UNKNOWN -> Errors.Unknown
                    }
                    _state.value = DrawsUiState.Error(error)
                }

                is DomainResult.Success -> _state.value =
                    DrawsUiState.Loaded(draws = draws.data.map { it.toUiModel() })
            }
        }
    }

    fun load() {
        _state.value = DrawsUiState.Loading
        fetch()

    }

    fun retry() {
        if (_state.value is DrawsUiState.Error) load()
    }

    fun refresh() {
        when (val current = _state.value) {
            is DrawsUiState.Error -> load()

            is DrawsUiState.Loaded -> {
                if (!current.refreshing) {
                    _state.value = current.copy(refreshing = true)
                    fetch()
                }
            }

            DrawsUiState.Loading -> Unit
        }
    }
}
