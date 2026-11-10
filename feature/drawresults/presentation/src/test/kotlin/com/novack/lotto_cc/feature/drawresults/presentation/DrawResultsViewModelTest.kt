package com.novack.lotto_cc.feature.drawresults.presentation

import app.cash.turbine.test
import com.novack.lotto_cc.core.testing.MainDispatcherRule
import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures.eurojackpot
import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures.lotto6aus49
import com.novack.lotto_cc.feature.drawresults.domain.FakeDrawResultsRepository
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainError
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.usecase.GetDrawResultsUseCase
import com.novack.lotto_cc.feature.drawresults.presentation.mapper.toUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawResultsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeDrawResultsRepository(DomainResult.Success(listOf(eurojackpot(), lotto6aus49())))

    private fun viewModel() = DrawResultsViewModel(GetDrawResultsUseCase(repository))

    private val expectedContent = DrawResultsUiState.Content(listOf(lotto6aus49().toUiModel(), eurojackpot().toUiModel()))

    @Test
    fun `loads on creation and shows the cards in display order`() = runTest(mainDispatcherRule.testDispatcher) {
        viewModel().uiState.test {
            assertEquals(DrawResultsUiState.Loading, awaitItem())
            assertEquals(expectedContent, awaitItem())
        }
        assertEquals(1, repository.calls)
    }

    @Test
    fun `failure shows the error reason`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.result = DomainResult.Failure(DomainError.NETWORK)

        viewModel().uiState.test {
            assertEquals(DrawResultsUiState.Loading, awaitItem())
            assertEquals(DrawResultsUiState.Error(ErrorReason.NoConnection), awaitItem())
        }
    }

    @Test
    fun `retry after an error loads again`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.result = DomainResult.Failure(DomainError.SERVER)
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(DrawResultsUiState.Loading, awaitItem())
            assertEquals(DrawResultsUiState.Error(ErrorReason.Server), awaitItem())

            repository.result = DomainResult.Success(listOf(lotto6aus49(), eurojackpot()))
            viewModel.retry()

            assertEquals(DrawResultsUiState.Loading, awaitItem())
            assertEquals(expectedContent, awaitItem())
        }
        assertEquals(2, repository.calls)
    }

    @Test
    fun `retry while loading does not start a second request`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = viewModel()

        viewModel.retry()
        advanceUntilIdle()

        assertEquals(1, repository.calls)
        assertEquals(expectedContent, viewModel.uiState.value)
    }

    @Test
    fun `retry while content is shown keeps the content`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.retry()
        advanceUntilIdle()

        assertEquals(1, repository.calls)
        assertEquals(expectedContent, viewModel.uiState.value)
    }
}
