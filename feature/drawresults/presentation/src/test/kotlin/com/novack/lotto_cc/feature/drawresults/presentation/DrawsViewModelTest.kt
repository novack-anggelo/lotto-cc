package com.novack.lotto_cc.feature.drawresults.presentation

import com.novack.lotto_cc.core.domain.models.DomainError
import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.core.testing.MainDispatcherRule
import com.novack.lotto_cc.feature.drawresults.domain.GetDrawsUseCase
import com.novack.lotto_cc.feature.drawresults.presentation.mappers.toUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.models.Errors
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val draws = listOf(lotto6aus49(), eurojackpot())
    private val loaded = DrawsUiState.Loaded(draws.map { it.toUiModel() })
    private val repository = FakeDrawRepository(DomainResult.Success(draws))

    private fun viewModel() = DrawsViewModel(GetDrawsUseCase(repository))

    @Test
    fun `loads on creation`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = viewModel()
        assertEquals(DrawsUiState.Loading, viewModel.state.value)

        advanceUntilIdle()

        assertEquals(loaded, viewModel.state.value)
        assertEquals(1, repository.calls)
    }

    @Test
    fun `maps each domain error to its UI error`() = runTest(mainDispatcherRule.testDispatcher) {
        mapOf(
            DomainError.NETWORK to Errors.Network,
            DomainError.SERVER to Errors.Server,
            DomainError.UNKNOWN to Errors.Unknown,
        ).forEach { (domainError, uiError) ->
            repository.result = DomainResult.Failure(domainError)
            val viewModel = viewModel()
            advanceUntilIdle()

            assertEquals(DrawsUiState.Error(uiError), viewModel.state.value)
        }
    }

    @Test
    fun `retry after an error shows loading, then the draws`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.result = DomainResult.Failure(DomainError.NETWORK)
        val viewModel = viewModel()
        advanceUntilIdle()

        repository.result = DomainResult.Success(draws)
        viewModel.retry()
        assertEquals(DrawsUiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        assertEquals(loaded, viewModel.state.value)
        assertEquals(2, repository.calls)
    }

    @Test
    fun `retry is ignored unless there is an error`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = viewModel()
        viewModel.retry() // while loading
        advanceUntilIdle()
        viewModel.retry() // while loaded
        advanceUntilIdle()

        assertEquals(loaded, viewModel.state.value)
        assertEquals(1, repository.calls)
    }

    @Test
    fun `refresh keeps the draws visible while loading new ones`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        val newDraws = listOf(lotto6aus49(jackpot = null))
        repository.result = DomainResult.Success(newDraws)
        viewModel.refresh()
        assertEquals(loaded.copy(refreshing = true), viewModel.state.value)
        advanceUntilIdle()

        assertEquals(DrawsUiState.Loaded(newDraws.map { it.toUiModel() }), viewModel.state.value)
        assertEquals(2, repository.calls)
    }

    @Test
    fun `refresh while refreshing or loading does not start another request`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = viewModel()
            viewModel.refresh() // while loading
            advanceUntilIdle()

            viewModel.refresh()
            viewModel.refresh() // while refreshing
            advanceUntilIdle()

            assertEquals(2, repository.calls)
        }

    @Test
    fun `refresh after an error loads again`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.result = DomainResult.Failure(DomainError.SERVER)
        val viewModel = viewModel()
        advanceUntilIdle()

        repository.result = DomainResult.Success(draws)
        viewModel.refresh()
        assertEquals(DrawsUiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        assertEquals(loaded, viewModel.state.value)
    }

    @Test
    fun `failed refresh replaces the draws with the error`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        repository.result = DomainResult.Failure(DomainError.NETWORK)
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(DrawsUiState.Error(Errors.Network), viewModel.state.value)
    }
}
