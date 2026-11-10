package com.novack.lotto_cc.feature.drawresults.presentation.mapper

import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainError
import com.novack.lotto_cc.feature.drawresults.domain.model.NextDraw
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import com.novack.lotto_cc.feature.drawresults.presentation.model.Lottery
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DrawResultsUiMappersTest {

    @Test
    fun `lotto 6aus49 maps the Superzahl as its only bonus number`() {
        val card = DrawFixtures.lotto6aus49().toUiModel()

        assertEquals(
            DrawCardUiModel(
                lottery = Lottery.Lotto6aus49,
                lastDrawTime = Instant.parse("2023-01-21T18:25:00Z"),
                numbers = listOf(4, 19, 24, 43, 44, 45),
                bonusNumbers = listOf(7),
                nextDrawTime = Instant.parse("2023-01-25T17:25:00Z"),
                jackpot = DrawFixtures.euros("6000000"),
            ),
            card,
        )
    }

    @Test
    fun `eurojackpot maps the Eurozahlen as bonus numbers`() {
        val card = DrawFixtures.eurojackpot().toUiModel()

        assertEquals(Lottery.Eurojackpot, card.lottery)
        assertEquals(listOf(1, 12, 15, 31, 47), card.numbers)
        assertEquals(listOf(9, 10), card.bonusNumbers)
        assertEquals(Instant.parse("2023-01-24T18:00:00Z"), card.nextDrawTime)
        assertEquals(DrawFixtures.euros("80000000"), card.jackpot)
    }

    @Test
    fun `unknown jackpot stays null`() {
        val draw = DrawFixtures.eurojackpot(nextDraw = NextDraw(Instant.parse("2023-01-24T18:00:00Z"), jackpot = null))

        assertNull(draw.toUiModel().jackpot)
    }

    @Test
    fun `every domain error maps to an error reason`() {
        assertEquals(ErrorReason.NoConnection, DomainError.NETWORK.toErrorReason())
        assertEquals(ErrorReason.Server, DomainError.SERVER.toErrorReason())
        assertEquals(ErrorReason.Unknown, DomainError.UNKNOWN.toErrorReason())
    }
}
