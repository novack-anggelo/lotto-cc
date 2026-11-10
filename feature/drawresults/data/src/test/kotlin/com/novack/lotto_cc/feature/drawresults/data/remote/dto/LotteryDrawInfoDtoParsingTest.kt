package com.novack.lotto_cc.feature.drawresults.data.remote.dto

import com.novack.lotto_cc.core.network.buildJson
import com.novack.lotto_cc.feature.drawresults.data.remote.readResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LotteryDrawInfoDtoParsingTest {

    private val json = buildJson()

    private fun parse(body: String): List<LotteryDrawInfoDto> = json.decodeFromString(body)

    @Test
    fun `parses the case study sample and ignores unused fields`() {
        val result = parse(readResource("draw_info_sample.json"))

        assertEquals(
            listOf(
                LotteryDrawInfoDto(
                    lottery = "6aus49",
                    lastDraw = LastDrawDto(
                        drawDateUtc = "2023-01-21T18:25:00Z",
                        drawResult = DrawResultDto(numbers = listOf(4, 19, 24, 43, 44, 45), superNumber = 7),
                    ),
                    nextDraw = NextDrawDto(
                        drawDateUtc = "2023-01-25T17:25:00Z",
                        jackpot = JackpotDto("EUR", mapOf("WC_1" to "6000000.00", "WC_2" to "1000000.00")),
                    ),
                ),
                LotteryDrawInfoDto(
                    lottery = "eurojackpot",
                    lastDraw = LastDrawDto(
                        drawDateUtc = "2023-01-20T18:00:00Z",
                        drawResult = DrawResultDto(numbers = listOf(1, 12, 15, 31, 47), euroNumbers = listOf(9, 10)),
                    ),
                    nextDraw = NextDrawDto(
                        drawDateUtc = "2023-01-24T18:00:00Z",
                        jackpot = JackpotDto("EUR", mapOf("WC_1" to "80000000.00", "WC_2" to "1000000.00")),
                    ),
                ),
            ),
            result,
        )
    }

    @Test
    fun `next draw without jackpot parses to null jackpot`() {
        val result = parse("""[{"lottery":"6aus49","nextDraw":{"drawDateUtc":"2023-01-25T17:25:00Z"}}]""")

        assertNull(result.single().nextDraw?.jackpot)
    }

    @Test
    fun `missing fields parse to null so the mapper can tell missing from empty`() {
        val result = parse(
            """[{"lottery":"6aus49","lastDraw":{"drawDateUtc":"2023-01-21T18:25:00Z","drawResult":{}}}]""",
        ).single()

        assertEquals(DrawResultDto(numbers = null, superNumber = null, euroNumbers = null), result.lastDraw?.drawResult)
        assertNull(result.nextDraw)
    }

    @Test
    fun `one incomplete lottery does not fail the whole response`() {
        val result = parse(
            """
            [
              {"lottery":"6aus49","lastDraw":{"drawDateUtc":"2023-01-21T18:25:00Z","drawResult":{"numbers":[1,2,3,4,5,6],"superNumber":7}}},
              {"lottery":"spiel77","lastDraw":{"drawResult":null},"nextDraw":{"jackpot":{"jackpots":{"WC_1":null}}}}
            ]
            """,
        )

        assertEquals(listOf("6aus49", "spiel77"), result.map { it.lottery })
        assertEquals(listOf(1, 2, 3, 4, 5, 6), result[0].lastDraw?.drawResult?.numbers)
        assertNull(result[1].lastDraw?.drawResult)
        assertEquals(mapOf("WC_1" to null), result[1].nextDraw?.jackpot?.jackpots)
    }

    @Test
    fun `explicit null jackpot parses to null`() {
        val result = parse(
            """[{"lottery":"6aus49","nextDraw":{"drawDateUtc":"2023-01-25T17:25:00Z","jackpot":null}}]""",
        )

        assertNull(result.single().nextDraw?.jackpot)
    }
}
