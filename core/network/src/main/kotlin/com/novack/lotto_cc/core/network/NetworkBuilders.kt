package com.novack.lotto_cc.core.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 15L
private const val CALL_TIMEOUT_SECONDS = 30L

/**
 * Lenient JSON so API additions don't break parsing:
 * - unknown fields are ignored
 * - `null` or unknown enum values fall back to the property's default
 * - missing nullable fields decode as `null`; null properties are omitted when encoding
 */
fun buildJson(): Json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    explicitNulls = false
}

fun buildOkHttpClient(isDebug: Boolean = BuildConfig.DEBUG): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
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
