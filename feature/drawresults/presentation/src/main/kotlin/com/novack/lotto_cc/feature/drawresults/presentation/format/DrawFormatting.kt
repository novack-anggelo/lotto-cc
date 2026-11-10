package com.novack.lotto_cc.feature.drawresults.presentation.format

import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

/** A draw date with weekday in [zone], e.g. "Mittwoch, 25. Januar 2023" or "Wednesday, January 25, 2023". */
internal fun Instant.toDrawDateText(zone: ZoneId, locale: Locale): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale).format(atZone(zone))

/** A jackpot in millions, for a localized template such as "%1$s Mio. %2$s" or "%2$s%1$s million". */
internal data class JackpotText(val millions: String, val currencySymbol: String)

/** Rounds down to one decimal ("53,3"), so the jackpot is never overstated. */
internal fun Money.toJackpotText(locale: Locale): JackpotText {
    val format = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = 1
        roundingMode = RoundingMode.DOWN
    }
    return JackpotText(
        millions = format.format(amount.movePointLeft(6)),
        currencySymbol = Currency.getInstance(currencyCode).getSymbol(locale),
    )
}
