package com.novack.lotto_cc.core.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/** Unknown fields are ignored, so API additions don't break parsing. */
fun buildJson(): Json = Json {
    ignoreUnknownKeys = true
}

fun buildOkHttpClient(isDebug: Boolean = BuildConfig.DEBUG): OkHttpClient =
    OkHttpClient.Builder()
        .apply {
            if (isDebug) {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            }
        }
        .build()

fun buildRetrofit(
    client: OkHttpClient,
    json: Json,
    baseUrl: String = BuildConfig.BASE_URL,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
