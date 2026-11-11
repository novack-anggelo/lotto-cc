package com.novack.lotto_cc.feature.drawresults.data.di

import org.koin.dsl.module

/**
 * Data layer of the draw results feature: API service and repository implementation.
 * The shared `Retrofit` comes from `networkModule` (`:core:network`), included by the app's root module.
 */
val drawResultsDataModule = module {
}
