package com.novack.lotto_cc.di

import com.novack.lotto_cc.core.network.di.networkModule
import com.novack.lotto_cc.feature.drawresults.data.di.drawResultsDataModule
import com.novack.lotto_cc.feature.drawresults.presentation.di.drawResultsPresentationModule
import org.koin.dsl.module

/** Root module: every core and feature module, plus app-level bindings. */
val appModule = module {
    includes(networkModule, drawResultsDataModule, drawResultsPresentationModule)
}
