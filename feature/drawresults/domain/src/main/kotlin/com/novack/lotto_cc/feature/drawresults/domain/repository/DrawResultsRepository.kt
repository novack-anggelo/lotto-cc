package com.novack.lotto_cc.feature.drawresults.domain.repository

import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw

interface DrawResultsRepository {
    suspend fun getDrawResults(): DomainResult<List<LotteryDraw>>
}
