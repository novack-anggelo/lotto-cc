package com.novack.lotto_cc.feature.drawresults.data.mapper

import com.novack.lotto_cc.feature.drawresults.data.remote.LotteryIds
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.JackpotDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfoDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.NextDrawDto
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.domain.model.NextDraw
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.Currency

/** Winning class of the top prize; its amount is the advertised jackpot. */
private const val TOP_PRIZE_CLASS = "WC_1"

/** Maps the lotteries the app knows; unknown or incomplete entries are dropped. */
internal fun List<LotteryDrawInfoDto>.toDomain(): List<LotteryDraw> = mapNotNull { it.toDomainOrNull() }

/**
 * Returns null when the lottery is unknown or anything needed for its card is missing or malformed.
 * Each lottery validates its own result shape, so lotteries with a different shape can be added
 * without touching the others.
 */
internal fun LotteryDrawInfoDto.toDomainOrNull(): LotteryDraw? = when (lottery) {
    LotteryIds.LOTTO_6AUS49 -> toLotto6aus49OrNull()
    LotteryIds.EUROJACKPOT -> toEurojackpotOrNull()
    else -> null
}

private fun LotteryDrawInfoDto.toLotto6aus49OrNull(): LotteryDraw.Lotto6aus49? {
    val result = lastDraw?.drawResult ?: return null
    return LotteryDraw.Lotto6aus49(
        lastDrawTime = lastDraw.drawDateUtc?.toInstantOrNull() ?: return null,
        numbers = result.numbers?.takeIf { it.isNotEmpty() } ?: return null,
        superNumber = result.superNumber ?: return null,
        nextDraw = nextDraw?.toDomainOrNull() ?: return null,
    )
}

private fun LotteryDrawInfoDto.toEurojackpotOrNull(): LotteryDraw.Eurojackpot? {
    val result = lastDraw?.drawResult ?: return null
    return LotteryDraw.Eurojackpot(
        lastDrawTime = lastDraw.drawDateUtc?.toInstantOrNull() ?: return null,
        numbers = result.numbers?.takeIf { it.isNotEmpty() } ?: return null,
        euroNumbers = result.euroNumbers?.takeIf { it.isNotEmpty() } ?: return null,
        nextDraw = nextDraw?.toDomainOrNull() ?: return null,
    )
}

internal fun NextDrawDto.toDomainOrNull(): NextDraw? {
    val time = drawDateUtc?.toInstantOrNull() ?: return null
    return NextDraw(time = time, jackpot = jackpot?.toDomainOrNull())
}

/** The top-prize amount, or null when it is not announced (missing, malformed, zero or unknown currency). */
internal fun JackpotDto.toDomainOrNull(): Money? {
    val amount = jackpots?.get(TOP_PRIZE_CLASS)?.toBigDecimalOrNull() ?: return null
    if (amount.signum() <= 0) return null
    val currencyCode = currency?.takeIf { it.isIsoCurrencyCode() } ?: return null
    return Money(amount = amount, currencyCode = currencyCode)
}

/** Accepts `Z` and numeric offsets on every API level (`Instant.parse` on API 26–33 only accepts `Z`). */
private fun String.toInstantOrNull(): Instant? =
    try {
        OffsetDateTime.parse(this).toInstant()
    } catch (_: DateTimeParseException) {
        null
    }

private fun String.isIsoCurrencyCode(): Boolean =
    try {
        Currency.getInstance(this)
        true
    } catch (_: IllegalArgumentException) {
        false
    }
