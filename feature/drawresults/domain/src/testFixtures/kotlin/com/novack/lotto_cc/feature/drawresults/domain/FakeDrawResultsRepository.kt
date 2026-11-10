package com.novack.lotto_cc.feature.drawresults.domain

import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository

/** In-memory repository: returns [result] and counts calls. */
class FakeDrawResultsRepository(
    var result: DomainResult<List<LotteryDraw>> = DomainResult.Success(emptyList()),
) : DrawResultsRepository {

    var calls = 0
        private set

    override suspend fun getDrawResults(): DomainResult<List<LotteryDraw>> {
        calls++
        return result
    }
}
