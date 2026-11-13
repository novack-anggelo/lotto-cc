package com.novack.lotto_cc.feature.drawresults.data.mappers

import com.novack.lotto_cc.core.domain.models.Money
import com.novack.lotto_cc.feature.drawresults.data.additionalGame
import com.novack.lotto_cc.feature.drawresults.data.drawInfo
import com.novack.lotto_cc.feature.drawresults.data.eurojackpot
import com.novack.lotto_cc.feature.drawresults.data.lotto6aus49
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo.LastDraw.DrawResult
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo.NextDraw.Jackpot
import com.novack.lotto_cc.feature.drawresults.domain.models.AdditionalGame
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw
import com.novack.lotto_cc.feature.drawresults.domain.models.NextDraw
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class DrawDataMapperTest {

    @Test
    fun `maps Lotto 6aus49 with super number, dates and jackpot`() {
        val draw = listOf(lotto6aus49()).toDomain().single()

        assertEquals(
            Draw.Lotto6aus49(
                numbers = listOf(4, 19, 24, 43, 44, 45),
                additionalNumbers = listOf(7),
                nextDraw = NextDraw(
                    date = Instant.parse("2023-01-25T17:25:00Z"),
                    jackpot = Money(BigDecimal("6000000"), "EUR"),
                ),
                date = Instant.parse("2023-01-21T18:25:00Z"),
                additionalGames = null,
            ),
            draw,
        )
    }

    @Test
    fun `maps Eurojackpot with euro numbers`() {
        val draw = listOf(eurojackpot()).toDomain().single()

        assertTrue(draw is Draw.EuroJackpot)
        assertEquals(listOf(1, 12, 15, 31, 47), draw.numbers)
        assertEquals(listOf(9, 10), draw.additionalNumbers)
    }

    @Test
    fun `attaches Spiel 77 and SUPER 6 to Lotto 6aus49 only`() {
        val draws = listOf(
            lotto6aus49(),
            eurojackpot(),
            additionalGame("spiel77", "7312244"),
            additionalGame("super6", "112430"),
        ).toDomain()

        assertEquals(2, draws.size)
        assertEquals(
            listOf(AdditionalGame.Spiel77("7312244"), AdditionalGame.Super6("112430")),
            (draws[0] as Draw.Lotto6aus49).additionalGames,
        )
    }

    @Test
    fun `keeps the draw without a jackpot when it is not known yet`() {
        val noJackpotObject = listOf(lotto6aus49(jackpot = null)).toDomain().single()
        val noTopPrize = listOf(lotto6aus49(jackpot = Jackpot("EUR", jackpots = null))).toDomain().single()
        val zeroJackpot = listOf(lotto6aus49(jackpot = Jackpot("EUR", mapOf("WC_1" to "0.00")))).toDomain().single()
        val malformed = listOf(lotto6aus49(jackpot = Jackpot("EUR", mapOf("WC_1" to "abc")))).toDomain().single()

        listOf(noJackpotObject, noTopPrize, zeroJackpot, malformed).forEach { assertNull(it.nextDraw.jackpot) }
    }

    @Test
    fun `skips entries with missing required data instead of failing`() {
        val draws = listOf(
            lotto6aus49(superNumber = null),
            eurojackpot(euroNumbers = null),
            drawInfo("6aus49", DrawResult(listOf(1), superNumber = 1), lastDrawUtc = "not a date"),
        ).toDomain()

        assertTrue(draws.isEmpty())
    }

    @Test
    fun `skips additional games without a number and unknown lotteries`() {
        val draws = listOf(
            lotto6aus49(),
            additionalGame("spiel77", null),
            additionalGame("keno", "123"),
        ).toDomain()

        assertEquals(1, draws.size)
        assertNull((draws.single() as Draw.Lotto6aus49).additionalGames)
    }
}
