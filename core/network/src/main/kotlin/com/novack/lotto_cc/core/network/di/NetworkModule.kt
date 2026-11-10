package com.novack.lotto_cc.core.network.di

import com.novack.lotto_cc.core.network.buildJson
import com.novack.lotto_cc.core.network.buildOkHttpClient
import com.novack.lotto_cc.core.network.buildRetrofit
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create

/** Shared HTTP stack. Feature data modules create their Retrofit services from the [retrofit2.Retrofit] here. */
val networkModule = module {
    single { create(::buildJson) }
    single { create(::buildOkHttpClient) }
    single { create(::buildRetrofit) }
}
