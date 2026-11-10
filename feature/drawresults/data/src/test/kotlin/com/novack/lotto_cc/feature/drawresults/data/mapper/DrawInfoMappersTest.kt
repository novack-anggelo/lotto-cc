package com.novack.lotto_cc.feature.drawresults.data.mapper

import com.novack.lotto_cc.core.network.buildJson
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.DrawResultDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.JackpotDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LastDrawDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfoDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.NextDrawDto
import com.novack.lotto_cc.feature.drawresults.data.remote.readResource
import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DrawInfoMappersTest {

    @Test
    fun `maps the case study sample to domain draws`() {
        val dtos: List<LotteryDrawInfoDto> = buildJson().decodeFromString(readResource("draw_info_sample.json"))

        assertEquals(listOf(DrawFixtures.lotto6aus49(), DrawFixtures.eurojackpot()), dtos.toDomain())
    }

    @Test
    fun `keeps Superzahl and Eurozahlen apart`() {
        assertEquals(DrawFixtures.lotto6aus49(), lottoDto().toDomainOrNull())
        assertEquals(DrawFixtures.eurojackpot(), eurojackpotDto().toDomainOrNull())
    }

    @Test
    fun `drops unknown lotteries`() {
        val dtos = listOf(lottoDto(), lottoDto().copy(lottery = "keno"), lottoDto().copy(lottery = null))

        assertEquals(listOf(DrawFixtures.lotto6aus49()), dtos.toDomain())
    }

    @Test
    fun `drops entries missing what the card needs`() {
        val lotto = lottoDto()
        val incomplete = listOf(
            lotto.copy(lastDraw = null),
            lotto.copy(lastDraw = lotto.lastDraw?.copy(drawDateUtc = null)),
            lotto.copy(lastDraw = lotto.lastDraw?.copy(drawDateUtc = "21.01.2023")),
            lotto.copy(lastDraw = lotto.lastDraw?.copy(drawResult = null)),
            lotto.copy(lastDraw = lotto.lastDraw?.copy(drawResult = DrawResultDto(numbers = null, superNumber = 7))),
            lotto.copy(lastDraw = lotto.lastDraw?.copy(drawResult = DrawResultDto(numbers = emptyList(), superNumber = 7))),
            lotto.copy(lastDraw = lotto.lastDraw?.copy(drawResult = DrawResultDto(numbers = listOf(1, 2, 3)))),
            lotto.copy(nextDraw = null),
            lotto.copy(nextDraw = lotto.nextDraw?.copy(drawDateUtc = null)),
            eurojackpotDto().copy(
                lastDraw = eurojackpotDto().lastDraw?.copy(drawResult = DrawResultDto(numbers = listOf(1, 2), euroNumbers = null)),
            ),
            eurojackpotDto().copy(
                lastDraw = eurojackpotDto().lastDraw?.copy(drawResult = DrawResultDto(numbers = listOf(1, 2), euroNumbers = emptyList())),
            ),
        )

        incomplete.forEach { assertNull(it.toDomainOrNull(), "expected $it to be dropped") }
    }

    @Test
    fun `parses UTC dates written as Z or as an offset`() {
        val withZ = NextDrawDto(drawDateUtc = "2023-01-25T17:25:00Z").toDomainOrNull()
        val withOffset = NextDrawDto(drawDateUtc = "2023-01-25T18:25:00+01:00").toDomainOrNull()

        assertEquals(Instant.parse("2023-01-25T17:25:00Z"), withZ?.time)
        assertEquals(withZ, withOffset)
    }

    @Test
    fun `next draw without jackpot keeps the draw with a null jackpot`() {
        val dto = lottoDto().copy(nextDraw = NextDrawDto(drawDateUtc = "2023-01-25T17:25:00Z", jackpot = null))

        val draw = dto.toDomainOrNull()

        assertEquals(Instant.parse("2023-01-25T17:25:00Z"), draw?.nextDraw?.time)
        assertNull(draw?.nextDraw?.jackpot)
    }

    @Test
    fun `jackpot is the top prize class`() {
        val jackpot = JackpotDto("EUR", mapOf("WC_2" to "1000000.00", "WC_1" to "6000000.00")).toDomainOrNull()

        assertEquals(DrawFixtures.euros("6000000"), jackpot)
    }

    @Test
    fun `jackpot is null when not announced or malformed`() {
        listOf(
            JackpotDto("EUR", null),
            JackpotDto("EUR", emptyMap()),
            JackpotDto("EUR", mapOf("WC_2" to "1000000.00")),
            JackpotDto("EUR", mapOf("WC_1" to null)),
            JackpotDto("EUR", mapOf("WC_1" to "six million")),
            JackpotDto("EUR", mapOf("WC_1" to "0.00")),
            JackpotDto(null, mapOf("WC_1" to "6000000.00")),
            JackpotDto("", mapOf("WC_1" to "6000000.00")),
            JackpotDto("euro", mapOf("WC_1" to "6000000.00")),
        ).forEach { assertNull(it.toDomainOrNull(), "expected no jackpot for $it") }
    }

    private fun lottoDto() = LotteryDrawInfoDto(
        lottery = "6aus49",
        lastDraw = LastDrawDto(
            drawDateUtc = "2023-01-21T18:25:00Z",
            drawResult = DrawResultDto(numbers = listOf(4, 19, 24, 43, 44, 45), superNumber = 7),
        ),
        nextDraw = NextDrawDto(
            drawDateUtc = "2023-01-25T17:25:00Z",
            jackpot = JackpotDto("EUR", mapOf("WC_1" to "6000000.00", "WC_2" to "1000000.00")),
        ),
    )

    private fun eurojackpotDto() = LotteryDrawInfoDto(
        lottery = "eurojackpot",
        lastDraw = LastDrawDto(
            drawDateUtc = "2023-01-20T18:00:00Z",
            drawResult = DrawResultDto(numbers = listOf(1, 12, 15, 31, 47), euroNumbers = listOf(9, 10)),
        ),
        nextDraw = NextDrawDto(
            drawDateUtc = "2023-01-24T18:00:00Z",
            jackpot = JackpotDto("EUR", mapOf("WC_1" to "80000000.00")),
        ),
    )
}
