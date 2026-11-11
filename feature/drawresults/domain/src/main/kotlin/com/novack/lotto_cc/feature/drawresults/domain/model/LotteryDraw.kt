package com.novack.lotto_cc.feature.drawresults.domain.model

import java.time.Instant

/**
 * The latest draw of a lottery plus the upcoming one.
 * Each lottery has its own subtype because their results differ in shape:
 * 6aus49 has one Superzahl, Eurojackpot has two Eurozahlen.
 * Times are [Instant]s; the time zone used for display is a presentation decision.
 */
sealed interface LotteryDraw {
    val lastDrawTime: Instant
    val numbers: List<Int>
    val nextDraw: NextDraw

    /**
     * @param spiel77 the Spiel 77 number (7 digits) drawn with this draw; null when not available.
     * @param super6 the SUPER 6 number (6 digits) drawn with this draw; null when not available.
     *   Both are digit strings because leading zeros are part of the number.
     */
    data class Lotto6aus49(
        override val lastDrawTime: Instant,
        override val numbers: List<Int>,
        val superNumber: Int,
        override val nextDraw: NextDraw,
        val spiel77: String? = null,
        val super6: String? = null,
    ) : LotteryDraw

    data class Eurojackpot(
        override val lastDrawTime: Instant,
        override val numbers: List<Int>,
        val euroNumbers: List<Int>,
        override val nextDraw: NextDraw,
    ) : LotteryDraw
}
