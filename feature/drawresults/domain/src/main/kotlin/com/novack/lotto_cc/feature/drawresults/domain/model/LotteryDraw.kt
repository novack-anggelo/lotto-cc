package com.novack.lotto_cc.feature.drawresults.domain.model

import java.time.OffsetDateTime

/**
 * The latest draw of a lottery plus the upcoming one.
 * Each lottery has its own subtype because their results differ in shape:
 * 6aus49 has one Superzahl, Eurojackpot has two Eurozahlen.
 */
sealed interface LotteryDraw {
    val lastDrawDate: OffsetDateTime
    val numbers: List<Int>
    val nextDraw: NextDraw

    data class Lotto6aus49(
        override val lastDrawDate: OffsetDateTime,
        override val numbers: List<Int>,
        val superNumber: Int,
        override val nextDraw: NextDraw,
    ) : LotteryDraw

    data class Eurojackpot(
        override val lastDrawDate: OffsetDateTime,
        override val numbers: List<Int>,
        val euroNumbers: List<Int>,
        override val nextDraw: NextDraw,
    ) : LotteryDraw
}
