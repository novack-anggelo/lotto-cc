package com.novack.lotto_cc.feature.drawresults.presentation.di

import com.novack.lotto_cc.feature.drawresults.domain.usecase.GetDrawResultsUseCase
import com.novack.lotto_cc.feature.drawresults.presentation.DrawResultsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.viewModel

/** Use case and ViewModel of the draw results screen. Needs a `DrawResultsRepository` (data module). */
val drawResultsPresentationModule = module {
    factory<GetDrawResultsUseCase>()
    viewModel<DrawResultsViewModel>()
}
