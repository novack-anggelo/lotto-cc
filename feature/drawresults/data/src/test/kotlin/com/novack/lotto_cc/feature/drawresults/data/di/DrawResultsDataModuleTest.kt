package com.novack.lotto_cc.feature.drawresults.data.di

import com.novack.lotto_cc.feature.drawresults.data.repository.DrawResultsRepositoryImpl
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class DrawResultsDataModuleTest {

    @Test
    fun `module provides a single repository bound to the domain interface`() {
        val koin = koinApplication { modules(drawResultsDataModule) }.koin

        val repository = koin.get<DrawResultsRepository>()

        assertIs<DrawResultsRepositoryImpl>(repository)
        assertSame(repository, koin.get<DrawResultsRepository>())
    }
}
