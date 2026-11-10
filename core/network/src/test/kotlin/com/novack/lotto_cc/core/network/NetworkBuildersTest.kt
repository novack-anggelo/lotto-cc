package com.novack.lotto_cc.core.network

import okhttp3.logging.HttpLoggingInterceptor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkBuildersTest {

    @Test
    fun `json ignores unknown keys and omits explicit nulls`() {
        val configuration = buildJson().configuration

        assertTrue(configuration.ignoreUnknownKeys)
        assertFalse(configuration.explicitNulls)
    }

    @Test
    fun `client logs only in debug builds`() {
        assertTrue(buildOkHttpClient(isDebug = true).interceptors.any { it is HttpLoggingInterceptor })
        assertFalse(buildOkHttpClient(isDebug = false).interceptors.any { it is HttpLoggingInterceptor })
    }

    @Test
    fun `client uses 15 second timeouts`() {
        val client = buildOkHttpClient(isDebug = false)

        assertEquals(15_000, client.connectTimeoutMillis)
        assertEquals(15_000, client.readTimeoutMillis)
    }

    @Test
    fun `retrofit uses the given client and base url`() {
        val client = buildOkHttpClient(isDebug = false)

        val retrofit = buildRetrofit(client, buildJson(), baseUrl = "https://example.com/api/")

        assertEquals("https://example.com/api/", retrofit.baseUrl().toString())
        assertEquals(client, retrofit.callFactory())
    }
}
