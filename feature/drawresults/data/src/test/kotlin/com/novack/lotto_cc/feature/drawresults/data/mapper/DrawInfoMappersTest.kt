package com.novack.lotto_cc.feature.drawresults.data.mapper

import com.novack.lotto_cc.core.network.buildJson
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.DrawResultDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.JackpotDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LastDrawDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfoDto
import com.novack.lotto_cc.feature.drawresults.data.remote.dto.NextDrawDto
import com.novack.lotto_cc.feature.drawresults.data.remote.readResource
import com.novack.lotto_cc.feature.drawresults.domain.DrawFixtures
import kotlinx.serialization.json.JsonPrimitive
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

    @Test
    fun `attaches Spiel 77 and SUPER 6 to the 6aus49 draw they were drawn with`() {
        val dtos = listOf(lottoDto(), eurojackpotDto(), supplementaryDto("spiel77", "0312244"), supplementaryDto("super6", "112430"))

        assertEquals(
            listOf(DrawFixtures.lotto6aus49(spiel77 = "0312244", super6 = "112430"), DrawFixtures.eurojackpot()),
            dtos.toDomain(),
        )
    }

    @Test
    fun `ignores Spiel 77 and SUPER 6 from another draw`() {
        val dtos = listOf(
            lottoDto(),
            supplementaryDto("spiel77", "7312244", drawDateUtc = "2023-01-18T17:25:00Z"),
            supplementaryDto("super6", "112430", drawDateUtc = "2023-01-18T17:25:00Z"),
        )

        assertEquals(listOf(DrawFixtures.lotto6aus49()), dtos.toDomain())
    }

    @Test
    fun `ignores malformed Spiel 77 and SUPER 6 numbers`() {
        listOf(
            supplementaryDto("spiel77", "731224"),
            supplementaryDto("spiel77", "73122440"),
            supplementaryDto("spiel77", "73x2244"),
            supplementaryDto("super6", "1124300"),
            supplementaryDto("super6", null),
            supplementaryDto("super6", "112430", drawDateUtc = null),
            supplementaryDto("super6", JsonPrimitive(112430), "2023-01-21T18:25:00Z"),
        ).forEach { malformed ->
            assertEquals(listOf(DrawFixtures.lotto6aus49()), listOf(lottoDto(), malformed).toDomain(), "for $malformed")
        }
    }

    @Test
    fun `uses the first valid entry when a supplementary lottery appears twice`() {
        val dtos = listOf(lottoDto(), supplementaryDto("spiel77", "73x2244"), supplementaryDto("spiel77", "7312244"))

        assertEquals(listOf(DrawFixtures.lotto6aus49(spiel77 = "7312244")), dtos.toDomain())
    }

    @Test
    fun `Spiel 77 and SUPER 6 without a 6aus49 draw are not shown`() {
        assertEquals(emptyList(), listOf(supplementaryDto("spiel77", "7312244"), supplementaryDto("super6", "112430")).toDomain())
    }

    private fun supplementaryDto(
        lottery: String,
        number: String?,
        drawDateUtc: String? = "2023-01-21T18:25:00Z",
    ) = supplementaryDto(lottery, number?.let(::JsonPrimitive), drawDateUtc)

    private fun supplementaryDto(lottery: String, number: JsonPrimitive?, drawDateUtc: String?) =
        LotteryDrawInfoDto(
            lottery = lottery,
            lastDraw = LastDrawDto(drawDateUtc = drawDateUtc, drawResult = DrawResultDto(number = number)),
            nextDraw = NextDrawDto(drawDateUtc = "2023-01-25T17:25:00Z"),
        )

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
