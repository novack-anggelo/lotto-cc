package com.novack.lotto_cc.feature.drawresults.data

import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo.LastDraw
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo.LastDraw.DrawResult
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo.NextDraw
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo.NextDraw.Jackpot

/** API entries shaped like the sample response in the case study. */
internal fun drawInfo(
    lottery: String,
    result: DrawResult,
    lastDrawUtc: String = "2023-01-21T18:25:00Z",
    nextDrawUtc: String = "2023-01-25T17:25:00Z",
    jackpot: Jackpot? = Jackpot(currency = "EUR", jackpots = mapOf("WC_1" to "6000000.00")),
) = LotteryDrawInfo(
    lottery = lottery,
    lastDraw = LastDraw(id = "last", drawDateUtc = lastDrawUtc, drawResult = result),
    nextDraw = NextDraw(id = "next", drawDateUtc = nextDrawUtc, jackpot = jackpot),
)

internal fun lotto6aus49(
    superNumber: Int? = 7,
    jackpot: Jackpot? = Jackpot(currency = "EUR", jackpots = mapOf("WC_1" to "6000000.00")),
) = drawInfo(
    lottery = "6aus49",
    result = DrawResult(numbers = listOf(4, 19, 24, 43, 44, 45), superNumber = superNumber),
    jackpot = jackpot,
)

internal fun eurojackpot(euroNumbers: List<Int>? = listOf(9, 10)) = drawInfo(
    lottery = "eurojackpot",
    result = DrawResult(numbers = listOf(1, 12, 15, 31, 47), euroNumbers = euroNumbers),
)

internal fun additionalGame(lottery: String, number: String?) = drawInfo(
    lottery = lottery,
    result = DrawResult(numbers = emptyList(), additionalGameNumber = number),
    jackpot = null,
)
