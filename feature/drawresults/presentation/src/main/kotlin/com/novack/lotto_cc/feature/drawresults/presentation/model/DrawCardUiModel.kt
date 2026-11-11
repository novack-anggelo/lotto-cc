package com.novack.lotto_cc.feature.drawresults.presentation.model

import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import java.time.Instant

/** Lotteries the screen knows how to present (name, colours, bonus-number label). */
enum class Lottery { Lotto6aus49, Eurojackpot }

/**
 * One lottery card. Dates and the jackpot stay typed: they are formatted when rendered, with the
 * current locale and time zone, so a configuration change never leaves stale text in the state.
 *
 * @param bonusNumbers the Superzahl or the Eurozahlen.
 * @param jackpot null when the next jackpot is unknown; the card then hides it.
 * @param spiel77 Spiel 77 number (LOTTO 6aus49 only); hidden when null.
 * @param super6 SUPER 6 number (LOTTO 6aus49 only); hidden when null.
 */
data class DrawCardUiModel(
    val lottery: Lottery,
    val lastDrawTime: Instant,
    val numbers: List<Int>,
    val bonusNumbers: List<Int>,
    val nextDrawTime: Instant,
    val jackpot: Money?,
    val spiel77: String? = null,
    val super6: String? = null,
)
