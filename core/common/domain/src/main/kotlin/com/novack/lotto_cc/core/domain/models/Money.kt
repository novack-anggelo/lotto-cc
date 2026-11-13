package com.novack.lotto_cc.core.domain.models

import java.math.BigDecimal

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