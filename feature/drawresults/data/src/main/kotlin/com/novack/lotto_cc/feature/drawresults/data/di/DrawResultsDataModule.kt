package com.novack.lotto_cc.feature.drawresults.data.di

import com.novack.lotto_cc.core.network.di.networkModule
import com.novack.lotto_cc.feature.drawresults.data.remote.DrawInfoApi
import com.novack.lotto_cc.feature.drawresults.data.repository.DrawResultsRepositoryImpl
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import retrofit2.Retrofit
import retrofit2.create

/** Data layer of the draw results feature. Includes the shared [networkModule] it builds on. */
val drawResultsDataModule = module {
    includes(networkModule)

    single { create(::buildDrawInfoApi) }
    single<DrawResultsRepositoryImpl>() bind DrawResultsRepository::class
}

internal fun buildDrawInfoApi(retrofit: Retrofit): DrawInfoApi = retrofit.create()
