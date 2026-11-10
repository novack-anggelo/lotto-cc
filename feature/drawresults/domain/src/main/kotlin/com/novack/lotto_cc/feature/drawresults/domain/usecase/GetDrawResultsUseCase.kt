package com.novack.lotto_cc.feature.drawresults.domain.usecase

import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository

/** Loads the latest draw results in display order: LOTTO 6aus49 first, then Eurojackpot. */
class GetDrawResultsUseCase(
    private val repository: DrawResultsRepository,
) {
    suspend operator fun invoke(): DomainResult<List<LotteryDraw>> =
        when (val result = repository.getDrawResults()) {
            is DomainResult.Success -> DomainResult.Success(result.data.sortedBy { it.displayOrder })
            is DomainResult.Failure -> result
        }

    private val LotteryDraw.displayOrder: Int
        get() = when (this) {
            is LotteryDraw.Lotto6aus49 -> 0
            is LotteryDraw.Eurojackpot -> 1
        }
}
