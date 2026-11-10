package com.novack.lotto_cc.feature.drawresults.data.remote

import com.novack.lotto_cc.core.network.buildJson
import com.novack.lotto_cc.core.network.buildOkHttpClient
import com.novack.lotto_cc.core.network.buildRetrofit
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import retrofit2.HttpException
import retrofit2.create
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DrawInfoApiTest {

    private val server = MockWebServer()
    private lateinit var api: DrawInfoApi

    @BeforeTest
    fun setUp() {
        server.start()
        api = buildRetrofit(
            client = buildOkHttpClient(isDebug = false),
            json = buildJson(),
            baseUrl = server.url("/public/").toString(),
        ).create()
    }

    @AfterTest
    fun tearDown() {
        server.close()
    }

    @Test
    fun `requests the aggregated endpoint with comma separated lotteries`() = runTest {
        server.enqueue(MockResponse.Builder().body(readResource("draw_info_sample.json")).build())

        val result = api.getAggregatedDrawInfo("${LotteryIds.LOTTO_6AUS49},${LotteryIds.EUROJACKPOT}")

        assertEquals("/public/v1/drawinfo/aggregated/6aus49,eurojackpot", server.takeRequest().url.encodedPath)
        assertEquals(listOf("6aus49", "eurojackpot"), result.map { it.lottery })
    }

    @Test
    fun `server errors surface as HttpException`() = runTest {
        server.enqueue(MockResponse.Builder().code(500).build())

        val error = assertFailsWith<HttpException> { api.getAggregatedDrawInfo(LotteryIds.LOTTO_6AUS49) }

        assertEquals(500, error.code())
    }
}
