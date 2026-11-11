package com.novack.lotto_cc.feature.drawresults.data.mapper

import com.novack.lotto_cc.feature.drawresults.data.remote.LotteryIds
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.JackpotDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LastDrawDto
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

/**
 * Maps the lotteries the app knows; unknown or incomplete entries are dropped. Spiel 77 and SUPER 6
 * are not shown on their own: their numbers are attached to the 6aus49 draw they were drawn with.
 */
internal fun List<LotteryDrawInfoDto>.toDomain(): List<LotteryDraw> {
    val spiel77 = supplementaryResult(LotteryIds.SPIEL77)
    val super6 = supplementaryResult(LotteryIds.SUPER6)
    return mapNotNull { it.toDomainOrNull() }.map { draw ->
        if (draw !is LotteryDraw.Lotto6aus49) return@map draw
        draw.copy(spiel77 = spiel77?.numberFor(draw), super6 = super6?.numberFor(draw))
    }
}

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

/** The winning number of a supplementary game (Spiel 77, SUPER 6) and when it was drawn. */
private class SupplementaryResult(val drawTime: Instant, val number: String) {
    /**
     * The number only if it was drawn with [draw] (the API stamps both with the same time), so an
     * out-of-sync result is never shown.
     */
    fun numberFor(draw: LotteryDraw.Lotto6aus49): String? = number.takeIf { drawTime == draw.lastDrawTime }
}

/** The first entry for [lottery] with both a number and a draw time; shown as the API sends it. */
private fun List<LotteryDrawInfoDto>.supplementaryResult(lottery: String): SupplementaryResult? =
    filter { it.lottery == lottery }.firstNotNullOfOrNull { it.lastDraw?.toSupplementaryResultOrNull() }

private fun LastDrawDto.toSupplementaryResultOrNull(): SupplementaryResult? {
    val number = drawResult?.number ?: return null
    val drawTime = drawDateUtc?.toInstantOrNull() ?: return null
    return SupplementaryResult(drawTime, number)
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
