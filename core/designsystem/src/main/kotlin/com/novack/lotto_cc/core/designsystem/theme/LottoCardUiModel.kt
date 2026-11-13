package com.novack.lotto_cc.core.designsystem.theme

import java.math.BigDecimal
import kotlin.time.Instant


data class LottoCardUiModel(
    val lottery: Lottery,
    val date: Instant,
    val numbers: List<Int>,
    val additionalNumbers: List<Int>,
    val nextDrawDate: Instant,
    val jackpotValue: BigDecimal?,
    val additionalGames: Map<AdditionalGameUi, String>? = null,
)

enum class Lottery {
    Lotto6aus49,
    EuroJackpot
}

enum class AdditionalGameUi {
    Spiel77, Super6
}
