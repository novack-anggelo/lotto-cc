package com.novack.lotto_cc.feature.drawresults.presentation.formatters

import com.novack.lotto_cc.core.domain.models.Money
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

private val OneMillion = BigDecimal(1_000_000)

internal fun Instant.toDrawDate(locale: Locale, zone: ZoneId = ZoneId.systemDefault()): String =
    DateTimeFormatter.ofPattern("EE, dd.MM.yyyy", locale).format(toJavaInstant().atZone(zone))

internal fun Money.toMillionsOrNull(locale: Locale): String? {
    if (amount < OneMillion) return null
    val format = NumberFormat.getNumberInstance(locale).apply { maximumFractionDigits = 1 }
    return format.format(amount.divide(OneMillion))
}

internal fun Money.toFullAmount(locale: Locale): String =
    NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance(currencyCode)
        maximumFractionDigits = 0
    }.format(amount)
