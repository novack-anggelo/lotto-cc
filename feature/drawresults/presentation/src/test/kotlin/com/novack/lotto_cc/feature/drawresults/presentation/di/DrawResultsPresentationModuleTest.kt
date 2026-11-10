package com.novack.lotto_cc.feature.drawresults.presentation.di

import com.novack.lotto_cc.core.testing.MainDispatcherRule
import com.novack.lotto_cc.feature.drawresults.domain.FakeDrawResultsRepository
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository
import com.novack.lotto_cc.feature.drawresults.presentation.DrawResultsViewModel
import org.junit.Rule
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotSame

class DrawResultsPresentationModuleTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `module provides a new ViewModel per request given a repository`() {
        val koin = koinApplication {
            modules(drawResultsPresentationModule, module { single<DrawResultsRepository> { FakeDrawResultsRepository() } })
        }.koin

        assertNotSame(koin.get<DrawResultsViewModel>(), koin.get<DrawResultsViewModel>())
    }
}
