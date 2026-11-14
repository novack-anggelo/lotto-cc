package com.novack.lotto_cc.feature.drawresults.presentation

import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.core.domain.models.Money
import com.novack.lotto_cc.feature.drawresults.domain.models.AdditionalGame
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw
import com.novack.lotto_cc.feature.drawresults.domain.models.NextDraw
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawRepository
import java.math.BigDecimal
import kotlin.time.Instant

internal val lastDrawDate = Instant.parse("2023-01-21T18:25:00Z")
internal val nextDrawDate = Instant.parse("2023-01-25T17:25:00Z")
internal val sixMillion = Money(BigDecimal("6000000.00"), "EUR")

internal fun lotto6aus49(
    jackpot: Money? = sixMillion,
    additionalGames: List<AdditionalGame>? = listOf(
        AdditionalGame.Spiel77("7312244"),
        AdditionalGame.Super6("112430"),
    ),
) = Draw.Lotto6aus49(
    numbers = listOf(4, 19, 24, 43, 44, 45),
    additionalNumbers = listOf(7),
    nextDraw = NextDraw(date = nextDrawDate, jackpot = jackpot),
    date = lastDrawDate,
    additionalGames = additionalGames,
)

internal fun eurojackpot(jackpot: Money? = sixMillion) = Draw.EuroJackpot(
    numbers = listOf(1, 12, 15, 31, 47),
    additionalNumbers = listOf(9, 10),
    nextDraw = NextDraw(date = nextDrawDate, jackpot = jackpot),
    date = lastDrawDate,
)

/** Returns [result] and counts how often it was asked. */
internal class FakeDrawRepository(var result: DomainResult<List<Draw>>) : DrawRepository {
    var calls = 0
        private set

    override suspend fun getDraws(): DomainResult<List<Draw>> {
        calls++
        return result
    }
}
