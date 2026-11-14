package com.novack.lotto_cc.feature.drawresults.presentation

import com.novack.lotto_cc.core.domain.models.Money
import com.novack.lotto_cc.feature.drawresults.presentation.models.AdditionalGameUi
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.models.Lottery
import java.math.BigDecimal
import kotlin.time.Instant

/** Sample cards for previews, with the values from the case study mockup. */
internal object PreviewData {
    val lotto6aus49 = DrawCardUiModel(
        lottery = Lottery.Lotto6aus49,
        date = Instant.parse("2020-09-18T18:25:00Z"),
        numbers = listOf(1, 2, 3, 14, 25, 36),
        additionalNumbers = listOf(4),
        nextDrawDate = Instant.parse("2020-09-23T17:25:00Z"),
        jackpotValue = Money(BigDecimal("23000000"), "EUR"),
        additionalGames = listOf(AdditionalGameUi.Spiel77("7312244"), AdditionalGameUi.Super6("112430")),
    )

    val eurojackpot = DrawCardUiModel(
        lottery = Lottery.EuroJackpot,
        date = Instant.parse("2020-09-18T18:00:00Z"),
        numbers = listOf(1, 2, 3, 14, 25),
        additionalNumbers = listOf(6, 12),
        nextDrawDate = Instant.parse("2020-09-23T18:00:00Z"),
        jackpotValue = Money(BigDecimal("53300000"), "EUR"),
    )

    /** Right after a draw: the next jackpot is not known yet. */
    val withoutJackpot = lotto6aus49.copy(jackpotValue = null, additionalGames = null)

    val draws = listOf(lotto6aus49, eurojackpot)
}
