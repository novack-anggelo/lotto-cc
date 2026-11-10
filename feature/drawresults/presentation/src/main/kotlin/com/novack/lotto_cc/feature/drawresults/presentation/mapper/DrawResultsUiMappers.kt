package com.novack.lotto_cc.feature.drawresults.presentation.mapper

import com.novack.lotto_cc.feature.drawresults.domain.model.DomainError
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import com.novack.lotto_cc.feature.drawresults.presentation.model.Lottery

internal fun LotteryDraw.toUiModel(): DrawCardUiModel = when (this) {
    is LotteryDraw.Lotto6aus49 -> toCard(Lottery.Lotto6aus49, bonusNumbers = listOf(superNumber))
    is LotteryDraw.Eurojackpot -> toCard(Lottery.Eurojackpot, bonusNumbers = euroNumbers)
}

private fun LotteryDraw.toCard(lottery: Lottery, bonusNumbers: List<Int>) = DrawCardUiModel(
    lottery = lottery,
    lastDrawTime = lastDrawTime,
    numbers = numbers,
    bonusNumbers = bonusNumbers,
    nextDrawTime = nextDraw.time,
    jackpot = nextDraw.jackpot,
)

internal fun DomainError.toErrorReason(): ErrorReason = when (this) {
    DomainError.NETWORK -> ErrorReason.NoConnection
    DomainError.SERVER -> ErrorReason.Server
    DomainError.UNKNOWN -> ErrorReason.Unknown
}
