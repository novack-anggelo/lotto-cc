package com.novack.lotto_cc.feature.drawresults.presentation.format

import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures.euros
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawFormattingTest {

    private val berlin = ZoneId.of("Europe/Berlin")
    private val newYork = ZoneId.of("America/New_York")

    @Test
    fun `draw date is a short date with weekday per locale`() {
        val time = Instant.parse("2023-01-25T17:25:00Z")

        assertEquals("Mi., 25.01.2023", time.toDrawDateText(berlin, Locale.GERMANY))
        assertEquals("Wed, Jan 25, 2023", time.toDrawDateText(berlin, Locale.US))
    }

    @Test
    fun `draw date uses the given time zone`() {
        val lateEveningUtc = Instant.parse("2023-01-21T23:30:00Z")

        assertEquals("So., 22.01.2023", lateEveningUtc.toDrawDateText(berlin, Locale.GERMANY))
        assertEquals("Sa., 21.01.2023", lateEveningUtc.toDrawDateText(newYork, Locale.GERMANY))
    }

    @Test
    fun `jackpot from one million is shown in millions with the locale's decimal separator`() {
        assertEquals(JackpotText("6.000.000\u00A0€", "6"), euros("6000000.00").toJackpotText(Locale.GERMANY))
        assertEquals(JackpotText("53.300.000\u00A0€", "53,3"), euros("53300000").toJackpotText(Locale.GERMANY))
        assertEquals(JackpotText("€53,300,000", "53.3"), euros("53300000").toJackpotText(Locale.US))
        assertEquals("1", euros("1000000").toJackpotText(Locale.GERMANY).millions)
    }

    @Test
    fun `jackpot below one million has no millions, only the full amount`() {
        assertEquals(JackpotText("500.000\u00A0€", millions = null), euros("500000").toJackpotText(Locale.GERMANY))
        assertEquals(JackpotText("€50,000", millions = null), euros("50000").toJackpotText(Locale.US))
    }

    @Test
    fun `jackpot is rounded down, never overstated`() {
        assertEquals(JackpotText("9.999.999\u00A0€", "9,9"), euros("9999999.99").toJackpotText(Locale.GERMANY))
        assertEquals(JackpotText("999.999\u00A0€", millions = null), euros("999999.99").toJackpotText(Locale.GERMANY))
    }
}
