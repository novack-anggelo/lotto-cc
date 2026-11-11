package com.novack.lotto_cc.feature.drawresults.domain

import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.domain.model.NextDraw
import java.math.BigDecimal
import java.time.Instant

/**
 * Domain test data based on the sample response in the case study (`drawDateUtc` values).
 * Shared with other modules' tests.
 */
object DrawFixtures {

    fun lotto6aus49(
        lastDrawTime: Instant = Instant.parse("2023-01-21T18:25:00Z"),
        numbers: List<Int> = listOf(4, 19, 24, 43, 44, 45),
        superNumber: Int = 7,
        nextDraw: NextDraw = NextDraw(
            time = Instant.parse("2023-01-25T17:25:00Z"),
            jackpot = euros("6000000.00"),
        ),
        spiel77: String? = null,
        super6: String? = null,
    ) = LotteryDraw.Lotto6aus49(lastDrawTime, numbers, superNumber, nextDraw, spiel77, super6)

    fun eurojackpot(
        lastDrawTime: Instant = Instant.parse("2023-01-20T18:00:00Z"),
        numbers: List<Int> = listOf(1, 12, 15, 31, 47),
        euroNumbers: List<Int> = listOf(9, 10),
        nextDraw: NextDraw = NextDraw(
            time = Instant.parse("2023-01-24T18:00:00Z"),
            jackpot = euros("80000000.00"),
        ),
    ) = LotteryDraw.Eurojackpot(lastDrawTime, numbers, euroNumbers, nextDraw)

    fun euros(amount: String) = Money(BigDecimal(amount), "EUR")
}
