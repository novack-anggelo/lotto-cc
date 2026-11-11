package com.novack.lotto_cc.core.network.di

import com.novack.lotto_cc.core.network.BuildConfig
import org.koin.dsl.koinApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class NetworkModuleTest {

    @Test
    fun `module provides Retrofit pointing at the draw info API`() {
        val koin = koinApplication { modules(networkModule) }.koin

        val retrofit = koin.get<Retrofit>()

        assertEquals(BuildConfig.BASE_URL, retrofit.baseUrl().toString())
    }

    @Test
    fun `Retrofit uses the shared OkHttpClient`() {
        val koin = koinApplication { modules(networkModule) }.koin

        assertSame(koin.get<OkHttpClient>(), koin.get<Retrofit>().callFactory())
    }
}
