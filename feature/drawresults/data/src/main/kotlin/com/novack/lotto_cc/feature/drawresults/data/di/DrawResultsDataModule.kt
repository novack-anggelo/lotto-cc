package com.novack.lotto_cc.feature.drawresults.data.di

import com.novack.lotto_cc.feature.drawresults.data.remote.DrawApi
import com.novack.lotto_cc.feature.drawresults.data.repository.DrawRepositoryImpl
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import retrofit2.Retrofit

/**
 * Data layer of the draw results feature: API service and repository implementation.
 * The shared `Retrofit` comes from `networkModule` (`:core:network`), included by the app's root module.
 */
val drawResultsDataModule = module {
    single { create(::buildDrawInfoApi) }
    single<DrawRepositoryImpl>() bind DrawRepository::class
}

internal fun buildDrawInfoApi(retrofit: Retrofit): DrawApi = retrofit.create(DrawApi::class.java)

