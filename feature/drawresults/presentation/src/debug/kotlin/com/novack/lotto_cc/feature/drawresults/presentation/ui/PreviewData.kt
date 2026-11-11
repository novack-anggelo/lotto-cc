package com.novack.lotto_cc.feature.drawresults.presentation.ui

import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.Lottery
import java.math.BigDecimal
import java.time.Instant

/** Sample cards for previews, based on the case study's sample response. */
internal object PreviewData {
    val lotto6aus49 = DrawCardUiModel(
        lottery = Lottery.Lotto6aus49,
        lastDrawTime = Instant.parse("2023-01-21T18:25:00Z"),
        numbers = listOf(4, 19, 24, 43, 44, 45),
        bonusNumbers = listOf(7),
        nextDrawTime = Instant.parse("2023-01-25T17:25:00Z"),
        jackpot = Money(BigDecimal("6000000"), "EUR"),
    )

    val eurojackpot = DrawCardUiModel(
        lottery = Lottery.Eurojackpot,
        lastDrawTime = Instant.parse("2023-01-20T18:00:00Z"),
        numbers = listOf(1, 12, 15, 31, 47),
        bonusNumbers = listOf(9, 10),
        nextDrawTime = Instant.parse("2023-01-24T18:00:00Z"),
        jackpot = Money(BigDecimal("80000000"), "EUR"),
    )
}
