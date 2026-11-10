package com.novack.lotto_cc.feature.drawresults.data.mapper

import com.novack.lotto_cc.feature.drawresults.data.remote.LotteryIds
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.JackpotDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfoDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.NextDrawDto
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.domain.model.NextDraw
import java.math.BigDecimal
import java.time.Instant
import java.time.format.DateTimeParseException

/** Winning class of the top prize; its amount is the advertised jackpot. */
private const val TOP_PRIZE_CLASS = "WC_1"

/** Maps the lotteries the app knows; unknown or incomplete entries are dropped. */
internal fun List<LotteryDrawInfoDto>.toDomain(): List<LotteryDraw> = mapNotNull { it.toDomainOrNull() }

/**
 * Returns null when the lottery is unknown or anything needed for its card is missing or malformed:
 * last draw date, numbers, Superzahl / Eurozahlen, or next draw date.
 */
internal fun LotteryDrawInfoDto.toDomainOrNull(): LotteryDraw? {
    val lastDrawTime = lastDraw?.drawDateUtc?.toInstantOrNull() ?: return null
    val result = lastDraw.drawResult ?: return null
    val numbers = result.numbers?.takeIf { it.isNotEmpty() } ?: return null
    val nextDraw = nextDraw?.toDomainOrNull() ?: return null

    return when (lottery) {
        LotteryIds.LOTTO_6AUS49 -> LotteryDraw.Lotto6aus49(
            lastDrawTime = lastDrawTime,
            numbers = numbers,
            superNumber = result.superNumber ?: return null,
            nextDraw = nextDraw,
        )

        LotteryIds.EUROJACKPOT -> LotteryDraw.Eurojackpot(
            lastDrawTime = lastDrawTime,
            numbers = numbers,
            euroNumbers = result.euroNumbers?.takeIf { it.isNotEmpty() } ?: return null,
            nextDraw = nextDraw,
        )

        else -> null
    }
}

internal fun NextDrawDto.toDomainOrNull(): NextDraw? {
    val time = drawDateUtc?.toInstantOrNull() ?: return null
    return NextDraw(time = time, jackpot = jackpot?.toDomainOrNull())
}

/** The top-prize amount, or null when it is not announced (missing, malformed or zero). */
internal fun JackpotDto.toDomainOrNull(): Money? {
    val amount = jackpots?.get(TOP_PRIZE_CLASS)?.toBigDecimalOrNull() ?: return null
    if (amount.signum() <= 0) return null
    return Money(amount = amount, currencyCode = currency ?: return null)
}

private fun String.toInstantOrNull(): Instant? =
    try {
        Instant.parse(this)
    } catch (_: DateTimeParseException) {
        null
    }

private fun String.toBigDecimalOrNull(): BigDecimal? =
    try {
        BigDecimal(this)
    } catch (_: NumberFormatException) {
        null
    }
