package com.novack.lotto_cc.feature.drawresults.domain.model

import java.math.BigDecimal

/**
 * An amount of money. Equality compares the amount by value, so `6000000` equals `6000000.00`
 * (unlike [BigDecimal.equals], which also compares scale).
 */
class Money(
    val amount: BigDecimal,
    val currencyCode: String,
) {
    private val normalizedAmount: BigDecimal = amount.stripTrailingZeros()

    override fun equals(other: Any?): Boolean =
        other is Money && normalizedAmount == other.normalizedAmount && currencyCode == other.currencyCode

    override fun hashCode(): Int = 31 * normalizedAmount.hashCode() + currencyCode.hashCode()

    override fun toString(): String = "Money(amount=${amount.toPlainString()}, currencyCode=$currencyCode)"
}
