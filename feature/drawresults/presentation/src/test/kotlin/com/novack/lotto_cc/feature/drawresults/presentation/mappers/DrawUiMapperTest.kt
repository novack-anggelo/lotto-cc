package com.novack.lotto_cc.feature.drawresults.presentation.mappers

import com.novack.lotto_cc.feature.drawresults.presentation.eurojackpot
import com.novack.lotto_cc.feature.drawresults.presentation.lastDrawDate
import com.novack.lotto_cc.feature.drawresults.presentation.lotto6aus49
import com.novack.lotto_cc.feature.drawresults.presentation.models.AdditionalGameUi
import com.novack.lotto_cc.feature.drawresults.presentation.models.Lottery
import com.novack.lotto_cc.feature.drawresults.presentation.models.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.nextDrawDate
import com.novack.lotto_cc.feature.drawresults.presentation.sixMillion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DrawUiMapperTest {

    @Test
    fun `maps Lotto 6aus49 with super number and additional games`() {
        assertEquals(
            DrawCardUiModel(
                lottery = Lottery.Lotto6aus49,
                date = lastDrawDate,
                numbers = listOf(4, 19, 24, 43, 44, 45),
                additionalNumbers = listOf(7),
                nextDrawDate = nextDrawDate,
                jackpotValue = sixMillion,
                additionalGames = listOf(
                    AdditionalGameUi.Spiel77("7312244"),
                    AdditionalGameUi.Super6("112430"),
                ),
            ),
            lotto6aus49().toUiModel(),
        )
    }

    @Test
    fun `maps Eurojackpot with euro numbers and no additional games`() {
        assertEquals(
            DrawCardUiModel(
                lottery = Lottery.EuroJackpot,
                date = lastDrawDate,
                numbers = listOf(1, 12, 15, 31, 47),
                additionalNumbers = listOf(9, 10),
                nextDrawDate = nextDrawDate,
                jackpotValue = sixMillion,
                additionalGames = null,
            ),
            eurojackpot().toUiModel(),
        )
    }

    @Test
    fun `keeps an unknown jackpot and missing additional games as null`() {
        val card = lotto6aus49(jackpot = null, additionalGames = null).toUiModel()

        assertNull(card.jackpotValue)
        assertNull(card.additionalGames)
    }
}
