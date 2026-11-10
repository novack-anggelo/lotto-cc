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
    fun `draw date is formatted with weekday per locale`() {
        val time = Instant.parse("2023-01-25T17:25:00Z")

        assertEquals("Mittwoch, 25. Januar 2023", time.toDrawDateText(berlin, Locale.GERMANY))
        assertEquals("Wednesday, January 25, 2023", time.toDrawDateText(berlin, Locale.US))
    }

    @Test
    fun `draw date uses the given time zone`() {
        val lateEveningUtc = Instant.parse("2023-01-21T23:30:00Z")

        assertEquals("Sonntag, 22. Januar 2023", lateEveningUtc.toDrawDateText(berlin, Locale.GERMANY))
        assertEquals("Samstag, 21. Januar 2023", lateEveningUtc.toDrawDateText(newYork, Locale.GERMANY))
    }

    @Test
    fun `jackpot is shown in millions with the locale's decimal separator`() {
        assertEquals(JackpotText("6", "€"), euros("6000000.00").toJackpotText(Locale.GERMANY))
        assertEquals(JackpotText("53,3", "€"), euros("53300000").toJackpotText(Locale.GERMANY))
        assertEquals(JackpotText("53.3", "€"), euros("53300000").toJackpotText(Locale.US))
    }

    @Test
    fun `jackpot is rounded down, never overstated`() {
        assertEquals("9,9", euros("9999999").toJackpotText(Locale.GERMANY).millions)
        assertEquals("0,5", euros("500000").toJackpotText(Locale.GERMANY).millions)
    }
}
