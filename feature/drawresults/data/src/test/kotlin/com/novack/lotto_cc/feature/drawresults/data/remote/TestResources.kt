package com.novack.lotto_cc.feature.drawresults.data.remote

import com.novack.lotto_cc.core.network.buildJson
import com.novack.lotto_cc.core.network.buildOkHttpClient
import com.novack.lotto_cc.core.network.buildRetrofit
import mockwebserver3.MockWebServer

internal fun readResource(name: String): String =
    requireNotNull(object {}.javaClass.classLoader?.getResource(name)) { "Missing test resource $name" }.readText()

/** A [DrawInfoApi] backed by the production Retrofit/Json setup, pointed at this (started) server. */
internal fun MockWebServer.drawInfoApi(): DrawInfoApi =
    buildRetrofit(
        client = buildOkHttpClient(isDebug = false),
        json = buildJson(),
        baseUrl = url("/public/").toString(),
    ).create(DrawInfoApi::class.java)
