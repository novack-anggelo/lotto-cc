package com.novack.lotto_cc.feature.drawresults.data.di

import com.novack.lotto_cc.feature.drawresults.data.remote.DrawInfoApi
import com.novack.lotto_cc.feature.drawresults.data.repository.DrawResultsRepositoryImpl
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import retrofit2.Retrofit

/**
 * Data layer of the draw results feature. Needs the shared `networkModule` (`:core:network`)
 * and a `Logger` (`:core:common`), both provided by the app's root module.
 */
val drawResultsDataModule = module {
    single { create(::buildDrawInfoApi) }
    single<DrawResultsRepositoryImpl>() bind DrawResultsRepository::class
}

internal fun buildDrawInfoApi(retrofit: Retrofit): DrawInfoApi = retrofit.create(DrawInfoApi::class.java)
