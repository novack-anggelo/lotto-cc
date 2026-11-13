package com.novack.lotto_cc.feature.drawresults.domain.models

import com.novack.lotto_cc.core.domain.models.Money
import kotlin.time.Instant

sealed interface Draw {
    val numbers: List<Int>
    val additionalNumbers: List<Int>
    val nextDraw: NextDraw
    val date: Instant

    data class Lotto6aus49(
        override val numbers: List<Int>,
        override val additionalNumbers: List<Int>,
        override val nextDraw: NextDraw,
        override val date: Instant,
        val additionalGames: List<AdditionalGame>?
    ) : Draw

    data class EuroJackpot(
        override val numbers: List<Int>,
        override val additionalNumbers: List<Int>,
        override val nextDraw: NextDraw,
        override val date: Instant
    ) : Draw
}

data class NextDraw(
    val date: Instant,
    val jackpot: Money?
)