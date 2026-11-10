package com.novack.lotto_cc.feature.drawresults.data.repository

import com.novack.lotto_cc.feature.drawresults.data.remote.DrawInfoApi
import com.novack.lotto_cc.feature.drawresults.data.remote.drawInfoApi
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfoDto
import com.novack.lotto_cc.feature.drawresults.data.remote.readResource
import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainError
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DrawResultsRepositoryImplTest {

    private val server = MockWebServer()
    private val logger = RecordingLogger()
    private lateinit var repository: DrawResultsRepositoryImpl

    @BeforeTest
    fun setUp() {
        server.start()
        repository = DrawResultsRepositoryImpl(server.drawInfoApi(), logger)
    }

    @AfterTest
    fun tearDown() {
        server.close()
    }

    @Test
    fun `returns mapped draws for both lotteries in one request`() = runTest {
        server.enqueue(MockResponse.Builder().body(readResource("draw_info_sample.json")).build())

        val result = repository.getDrawResults()

        assertEquals(DomainResult.Success(listOf(DrawFixtures.lotto6aus49(), DrawFixtures.eurojackpot())), result)
        assertEquals("/public/v1/drawinfo/aggregated/6aus49,eurojackpot", server.takeRequest().url.encodedPath)
        assertEquals(1, server.requestCount)
        assertTrue(logger.messages.isEmpty())
    }

    @Test
    fun `http error is a server error and is logged`() = runTest {
        server.enqueue(MockResponse.Builder().code(503).build())

        assertEquals(DomainResult.Failure(DomainError.SERVER), repository.getDrawResults())
        assertEquals(listOf("HTTP 503"), logger.messages)
    }

    @Test
    fun `malformed json is a server error`() = runTest {
        server.enqueue(MockResponse.Builder().body("""{"not":"a list"}""").build())

        assertEquals(DomainResult.Failure(DomainError.SERVER), repository.getDrawResults())
    }

    @Test
    fun `response without usable draws is a server error`() = runTest {
        server.enqueue(MockResponse.Builder().body("""[{"lottery":"keno"}]""").build())

        assertEquals(DomainResult.Failure(DomainError.SERVER), repository.getDrawResults())
    }

    @Test
    fun `io failure is a network error`() = runTest {
        val repository = DrawResultsRepositoryImpl(ThrowingApi(IOException("offline")), logger)

        assertEquals(DomainResult.Failure(DomainError.NETWORK), repository.getDrawResults())
    }

    @Test
    fun `unexpected exceptions are unknown errors`() = runTest {
        val repository = DrawResultsRepositoryImpl(ThrowingApi(IllegalStateException("boom")), logger)

        assertEquals(DomainResult.Failure(DomainError.UNKNOWN), repository.getDrawResults())
    }

    @Test
    fun `cancellation is rethrown, not turned into an error`() = runTest {
        val repository = DrawResultsRepositoryImpl(ThrowingApi(CancellationException("cancelled")), logger)

        assertFailsWith<CancellationException> { repository.getDrawResults() }
        assertTrue(logger.messages.isEmpty())
    }

    private class RecordingLogger : FailureLogger {
        val messages = mutableListOf<String>()
        override fun log(message: String, cause: Throwable?) {
            messages += message
        }
    }

    private class ThrowingApi(private val error: Throwable) : DrawInfoApi {
        override suspend fun getAggregatedDrawInfo(lotteries: String): List<LotteryDrawInfoDto> = throw error
    }
}
