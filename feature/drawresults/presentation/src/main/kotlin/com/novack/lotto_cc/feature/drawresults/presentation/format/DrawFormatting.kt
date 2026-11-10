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

/** A jackpot ready to display. Amounts are rounded down, so a jackpot is never overstated. */
internal sealed interface JackpotText {
    /** One million or more, for a localized template such as "%1$s Mio. %2$s" or "%2$s%1$s million". */
    data class Millions(val millions: String, val currencySymbol: String) : JackpotText

    /** Below one million: the full localized amount, e.g. "500.000 €". */
    data class Exact(val amount: String) : JackpotText
}

private val OneMillion = BigDecimal(1_000_000)

internal fun Money.toJackpotText(locale: Locale): JackpotText {
    val currency = Currency.getInstance(currencyCode)
    if (amount < OneMillion) {
        val format = NumberFormat.getCurrencyInstance(locale).apply {
            this.currency = currency
            maximumFractionDigits = 0
            roundingMode = RoundingMode.DOWN
        }
        return JackpotText.Exact(format.format(amount))
    }
    val format = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = 1
        roundingMode = RoundingMode.DOWN
    }
    return JackpotText.Millions(format.format(amount.movePointLeft(6)), currency.getSymbol(locale))
}
