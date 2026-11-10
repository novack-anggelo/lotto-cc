package com.novack.lotto_cc.feature.drawresults.domain.usecase

import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures
import com.novack.lotto_cc.feature.drawresults.domain.FakeDrawResultsRepository
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainError
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetDrawResultsUseCaseTest {

    private val repository = FakeDrawResultsRepository()
    private val getDrawResults = GetDrawResultsUseCase(repository)

    @Test
    fun `returns draws with 6aus49 before Eurojackpot`() = runTest {
        val lotto = DrawFixtures.lotto6aus49()
        val eurojackpot = DrawFixtures.eurojackpot()
        repository.result = DomainResult.Success(listOf(eurojackpot, lotto))

        val result = getDrawResults()

        assertEquals(DomainResult.Success(listOf(lotto, eurojackpot)), result)
    }

    @Test
    fun `returns a partial list unchanged`() = runTest {
        val eurojackpot = DrawFixtures.eurojackpot()
        repository.result = DomainResult.Success(listOf(eurojackpot))

        assertEquals(DomainResult.Success(listOf(eurojackpot)), getDrawResults())
    }

    @Test
    fun `passes failures through`() = runTest {
        repository.result = DomainResult.Failure(DomainError.NETWORK)

        assertEquals(DomainResult.Failure(DomainError.NETWORK), getDrawResults())
    }

    @Test
    fun `loads from the repository on every call`() = runTest {
        getDrawResults()
        getDrawResults()

        assertEquals(2, repository.calls)
    }
}
