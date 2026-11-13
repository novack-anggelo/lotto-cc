package com.novack.lotto_cc.core.designsystem.theme

import com.novack.lotto_cc.core.domain.models.Money
import java.math.BigDecimal
import kotlin.time.Instant


data class LottoCardUiModel(
    val lottery: Lottery,
    val date: Instant,
    val numbers: List<Int>,
    val additionalNumbers: List<Int>,
    val nextDrawDate: Instant,
    val jackpotValue: Money?,
    val additionalGames: List<AdditionalGameUi>? = null,
)

enum class Lottery {
    Lotto6aus49,
    EuroJackpot
}

sealed interface AdditionalGameUi {
    val number: String
    data class Spiel77(override val number: String) : AdditionalGameUi
    data class Super6(override val number: String) : AdditionalGameUi
}
