package com.novack.lotto_cc.feature.drawresults.presentation.mappers

import com.novack.lotto_cc.core.designsystem.theme.AdditionalGameUi
import com.novack.lotto_cc.core.designsystem.theme.Lottery
import com.novack.lotto_cc.core.designsystem.theme.LottoCardUiModel
import com.novack.lotto_cc.feature.drawresults.domain.models.AdditionalGame
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw

fun Draw.toUiModel() = when (this) {
    is Draw.EuroJackpot -> toUiModel()
    is Draw.Lotto6aus49 -> toUiModel()
}

private fun Draw.EuroJackpot.toUiModel() =
    LottoCardUiModel(
        lottery = Lottery.EuroJackpot,
        date = date,
        numbers = numbers,
        additionalNumbers = additionalNumbers,
        nextDrawDate = nextDraw.date,
        jackpotValue = nextDraw.jackpot,
    )

private fun Draw.Lotto6aus49.toUiModel() =
    LottoCardUiModel(
        lottery = Lottery.Lotto6aus49,
        date = date,
        numbers = numbers,
        additionalNumbers = additionalNumbers,
        nextDrawDate = nextDraw.date,
        jackpotValue = nextDraw.jackpot,
        additionalGames = additionalGames?.mapKeys { it.key.toUiModel() },
    )


private fun AdditionalGame.toUiModel() = when (this) {
    AdditionalGame.Spiel77 -> AdditionalGameUi.Spiel77
    AdditionalGame.Super6 -> AdditionalGameUi.Super6
}