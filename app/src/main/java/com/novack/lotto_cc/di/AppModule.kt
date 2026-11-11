package com.novack.lotto_cc.di

import com.novack.lotto_cc.core.common.logging.Logger
import com.novack.lotto_cc.core.network.di.networkModule
import com.novack.lotto_cc.feature.drawresults.data.di.drawResultsDataModule
import com.novack.lotto_cc.feature.drawresults.presentation.di.drawResultsPresentationModule
import com.novack.lotto_cc.logging.AndroidLogger
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

/** Root module: the app-level bindings plus every feature and core module. */
val appModule = module {
    includes(networkModule, drawResultsDataModule, drawResultsPresentationModule)
    single<AndroidLogger>() bind Logger::class
}
