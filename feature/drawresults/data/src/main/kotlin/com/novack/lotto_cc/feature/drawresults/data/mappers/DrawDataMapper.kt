package com.novack.lotto_cc.feature.drawresults.data.mappers

import com.novack.lotto_cc.core.domain.models.Money
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo
import com.novack.lotto_cc.feature.drawresults.domain.models.AdditionalGame
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw.Lotto6aus49
import com.novack.lotto_cc.feature.drawresults.domain.models.NextDraw
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import kotlin.time.Instant
import kotlin.time.toKotlinInstant

private const val TOP_PRICE = "WC_1"

private enum class LotteryIds(val id: String) {
    Lotto6aus49("6aus49"),
    EuroJackpot("eurojackpot"),
}

private enum class AdditionalGamesIds(val id: String) {
    Spiel77("spiel77"),
    Super6("super6"),
}

fun List<LotteryDrawInfo>.toDomain(): List<Draw> {
    val additionalGames = filter { lotteryDraw ->
        AdditionalGamesIds.entries.any { lotteryDraw.lottery == it.id }
    }
    return mapNotNull { lotteryDraw ->
        when (lotteryDraw.lottery) {
            LotteryIds.Lotto6aus49.id -> lotteryDraw.toLotto6aus49OrNull(additionalGames)
            LotteryIds.EuroJackpot.id -> lotteryDraw.toEuroJackpotOrNull()
            else -> null
        }
    }
}

private fun LotteryDrawInfo.toLotto6aus49OrNull(additionalGames: List<LotteryDrawInfo>): Draw? {
    val domainNextDraw = nextDraw.toDomain() ?: return null
    val date = lastDraw.drawDateUtc.toInstantOrNull() ?: return null
    val superNumber = lastDraw.drawResult.superNumber ?: return null

    return Lotto6aus49(
        numbers = lastDraw.drawResult.numbers,
        additionalNumbers = listOf(superNumber),
        nextDraw = domainNextDraw,
        date = date,
        additionalGames = additionalGames.toAdditionalGamesOrNull()
    )
}

private fun LotteryDrawInfo.toEuroJackpotOrNull(): Draw? {
    val domainNextDraw = nextDraw.toDomain() ?: return null
    val date = lastDraw.drawDateUtc.toInstantOrNull() ?: return null
    val euroNumbers = lastDraw.drawResult.euroNumbers ?: return null

    return Draw.EuroJackpot(
        numbers = lastDraw.drawResult.numbers,
        additionalNumbers = euroNumbers,
        nextDraw = domainNextDraw,
        date = date,
    )
}

private fun List<LotteryDrawInfo>.toAdditionalGamesOrNull(): List<AdditionalGame>? {
    return mapNotNull { lotteryDrawInfo ->
        val number = lotteryDrawInfo.lastDraw.drawResult.additionalGameNumber ?: return@mapNotNull null
        when (lotteryDrawInfo.lottery) {
            AdditionalGamesIds.Spiel77.id -> AdditionalGame.Spiel77(number)
            AdditionalGamesIds.Super6.id -> AdditionalGame.Super6(number)
            else -> null
        }
    }.takeIf { it.isNotEmpty() }
}

private fun LotteryDrawInfo.NextDraw.toDomain(): NextDraw? {
    val date = drawDateUtc.toInstantOrNull() ?: return null
    val domainJackpot = jackpot?.let { jackpot ->
        jackpot.jackpots?.get(TOP_PRICE)
            ?.toBigDecimalOrNull()
            ?.takeIf { it.signum() > 0 }
            ?.let { Money(amount = it, currencyCode = jackpot.currency) }
    }
    return NextDraw(date = date, jackpot = domainJackpot)
}

private fun String.toInstantOrNull(): Instant? =
    try {
        OffsetDateTime.parse(this).toInstant().toKotlinInstant()
    } catch (_: DateTimeParseException) {
        null
    }
