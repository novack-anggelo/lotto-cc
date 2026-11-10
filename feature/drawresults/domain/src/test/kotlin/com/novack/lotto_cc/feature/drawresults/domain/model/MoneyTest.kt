package com.novack.lotto_cc.feature.drawresults.domain.model

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MoneyTest {

    @Test
    fun `amounts with different scale are equal`() {
        val a = Money(BigDecimal("6000000"), "EUR")
        val b = Money(BigDecimal("6000000.00"), "EUR")

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `different amounts or currencies are not equal`() {
        assertNotEquals(Money(BigDecimal("1.00"), "EUR"), Money(BigDecimal("1.01"), "EUR"))
        assertNotEquals(Money(BigDecimal("1.00"), "EUR"), Money(BigDecimal("1.00"), "CHF"))
    }
}
