package com.novack.lotto_cc.feature.drawresults.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * One lottery in the `drawinfo/aggregated` response. Only the fields the app uses are declared;
 * the rest (quotas, winners, stakes...) is ignored by the lenient Json configuration.
 *
 * Every field is nullable with a null default so a single incomplete lottery never fails the
 * whole response; the mappers validate each entry and drop the ones that can't be shown.
 * Dates and amounts stay strings here; the mappers parse them into domain types.
 */
@Serializable
data class LotteryDrawInfoDto(
    val lottery: String? = null,
    val lastDraw: LastDrawDto? = null,
    val nextDraw: NextDrawDto? = null,
)

@Serializable
data class LastDrawDto(
    val drawDateUtc: String? = null,
    val drawResult: DrawResultDto? = null,
)

/**
 * `superNumber` is only sent for 6aus49, `euroNumbers` only for Eurojackpot, and `number` only for
 * Spiel 77 / SUPER 6 (a digit string such as `"0123456"`).
 */
@Serializable
data class DrawResultDto(
    val numbers: List<Int>? = null,
    val superNumber: Int? = null,
    val euroNumbers: List<Int>? = null,
    val number: String? = null,
)

@Serializable
data class NextDrawDto(
    val drawDateUtc: String? = null,
    val jackpot: JackpotDto? = null,
)

/** `jackpots` maps winning classes (`WC_1` = top prize) to amounts such as `"6000000.00"`. */
@Serializable
data class JackpotDto(
    val currency: String? = null,
    val jackpots: Map<String, String?>? = null,
)
