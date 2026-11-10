package com.novack.lotto_cc.feature.drawresults.domain

import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.domain.model.NextDraw
import java.math.BigDecimal
import java.time.OffsetDateTime

/** Domain test data based on the sample response in the case study. Shared with other modules' tests. */
object DrawFixtures {

    fun lotto6aus49(
        lastDrawDate: OffsetDateTime = OffsetDateTime.parse("2023-01-21T19:25:00+01:00"),
        numbers: List<Int> = listOf(4, 19, 24, 43, 44, 45),
        superNumber: Int = 7,
        nextDraw: NextDraw = NextDraw(
            date = OffsetDateTime.parse("2023-01-25T18:25:00+01:00"),
            jackpot = euros("6000000.00"),
        ),
    ) = LotteryDraw.Lotto6aus49(lastDrawDate, numbers, superNumber, nextDraw)

    fun eurojackpot(
        lastDrawDate: OffsetDateTime = OffsetDateTime.parse("2023-01-20T20:00:00+02:00"),
        numbers: List<Int> = listOf(1, 12, 15, 31, 47),
        euroNumbers: List<Int> = listOf(9, 10),
        nextDraw: NextDraw = NextDraw(
            date = OffsetDateTime.parse("2023-01-24T20:00:00+02:00"),
            jackpot = euros("80000000.00"),
        ),
    ) = LotteryDraw.Eurojackpot(lastDrawDate, numbers, euroNumbers, nextDraw)

    fun euros(amount: String) = Money(BigDecimal(amount), "EUR")
}
