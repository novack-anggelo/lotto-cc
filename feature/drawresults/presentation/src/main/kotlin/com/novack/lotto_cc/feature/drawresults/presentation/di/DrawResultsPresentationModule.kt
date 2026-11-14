package com.novack.lotto_cc.feature.drawresults.presentation.di

import com.novack.lotto_cc.feature.drawresults.domain.GetDrawsUseCase
import com.novack.lotto_cc.feature.drawresults.presentation.DrawsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.viewModel

/** Use cases and ViewModels of the draw results feature. The domain module stays free of DI. */
val drawResultsPresentationModule = module {
    factory<GetDrawsUseCase>()
    viewModel<DrawsViewModel>()
}
