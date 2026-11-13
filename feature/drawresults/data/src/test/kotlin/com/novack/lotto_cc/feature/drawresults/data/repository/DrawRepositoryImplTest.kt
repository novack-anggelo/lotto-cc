package com.novack.lotto_cc.feature.drawresults.data.repository

import com.novack.lotto_cc.core.domain.models.DomainError
import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.feature.drawresults.data.eurojackpot
import com.novack.lotto_cc.feature.drawresults.data.lotto6aus49
import com.novack.lotto_cc.feature.drawresults.data.remote.DrawApi
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DrawRepositoryImplTest {

    private class FakeDrawApi(private val response: () -> List<LotteryDrawInfo>) : DrawApi {
        var requestedLotteries: String? = null

        override suspend fun getAggregatedDrawInfo(lotteries: String): List<LotteryDrawInfo> {
            requestedLotteries = lotteries
            return response()
        }
    }

    private suspend fun getDrawsWith(response: () -> List<LotteryDrawInfo>) =
        DrawRepositoryImpl(FakeDrawApi(response)).getDraws()

    @Test
    fun `returns the mapped draws and asks for all supported lotteries`() = runTest {
        val api = FakeDrawApi { listOf(lotto6aus49(), eurojackpot()) }

        val result = DrawRepositoryImpl(api).getDraws()

        assertEquals("6aus49,eurojackpot,spiel77,super6", api.requestedLotteries)
        assertTrue(result is DomainResult.Success)
        assertEquals(2, result.data.size)
    }

    @Test
    fun `empty response is a server failure`() = runTest {
        assertError(DomainError.SERVER, getDrawsWith { emptyList() })
    }

    @Test
    fun `response with no usable draws is a server failure`() = runTest {
        assertError(DomainError.SERVER, getDrawsWith { listOf(lotto6aus49(superNumber = null)) })
    }

    @Test
    fun `IO error is a network failure`() = runTest {
        assertError(DomainError.NETWORK, getDrawsWith { throw IOException("offline") })
    }

    @Test
    fun `HTTP error is a server failure`() = runTest {
        val httpError = HttpException(Response.error<Any>(500, "".toResponseBody()))

        assertError(DomainError.SERVER, getDrawsWith { throw httpError })
    }

    @Test
    fun `malformed JSON is a server failure`() = runTest {
        assertError(DomainError.SERVER, getDrawsWith { throw SerializationException("bad json") })
    }

    @Test
    fun `any other exception is an unknown failure`() = runTest {
        assertError(DomainError.UNKNOWN, getDrawsWith { throw IllegalStateException("boom") })
    }

    @Test
    fun `cancellation is rethrown, not turned into a failure`() = runTest {
        assertFailsWith<CancellationException> { getDrawsWith { throw CancellationException() } }
    }

    private fun assertError(expected: DomainError, result: DomainResult<*>) {
        assertTrue(result is DomainResult.Failure, "expected Failure but was $result")
        assertEquals(expected, result.error)
    }
}
