package com.novack.lotto_cc.feature.drawresults.presentation.format

import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

/** A short draw date with weekday in [zone], e.g. "Mi., 25.01.2023" or "Wed, Jan 25, 2023". */
internal fun Instant.toDrawDateText(zone: ZoneId, locale: Locale): String {
    val datePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        FormatStyle.MEDIUM, null, IsoChronology.INSTANCE, locale,
    )
    return DateTimeFormatter.ofPattern("EEE, $datePattern", locale).format(atZone(zone))
}

/**
 * A jackpot ready to display. Amounts are rounded down, so a jackpot is never overstated.
 *
 * @param fullAmount the whole amount with currency, e.g. "€80,000,000" or "80.000.000 €"; read by
 *   screen readers, and shown when the jackpot is below a million.
 * @param millions the amount in millions for the strip, e.g. "53,3"; null below one million.
 */
internal data class JackpotText(val fullAmount: String, val millions: String?)

private val OneMillion = BigDecimal(1_000_000)

internal fun Money.toJackpotText(locale: Locale): JackpotText {
    val currencyFormat = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance(currencyCode)
        maximumFractionDigits = 0
        roundingMode = RoundingMode.DOWN
    }
    val millionsFormat = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = 1
        roundingMode = RoundingMode.DOWN
    }
    return JackpotText(
        fullAmount = currencyFormat.format(amount),
        millions = if (amount < OneMillion) null else millionsFormat.format(amount.movePointLeft(6)),
    )
}
