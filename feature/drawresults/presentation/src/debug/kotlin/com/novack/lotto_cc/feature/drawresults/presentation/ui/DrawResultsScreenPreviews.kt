package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme
import com.novack.lotto_cc.feature.drawresults.domain.model.Money
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawCardUiModel
import com.novack.lotto_cc.feature.drawresults.presentation.model.DrawResultsUiState
import com.novack.lotto_cc.feature.drawresults.presentation.model.ErrorReason
import com.novack.lotto_cc.feature.drawresults.presentation.model.Lottery
import java.math.BigDecimal
import java.time.Instant

private val previewCards = listOf(
    DrawCardUiModel(
        lottery = Lottery.Lotto6aus49,
        lastDrawTime = Instant.parse("2023-01-21T18:25:00Z"),
        numbers = listOf(4, 19, 24, 43, 44, 45),
        bonusNumbers = listOf(7),
        nextDrawTime = Instant.parse("2023-01-25T17:25:00Z"),
        jackpot = Money(BigDecimal("6000000"), "EUR"),
    ),
    DrawCardUiModel(
        lottery = Lottery.Eurojackpot,
        lastDrawTime = Instant.parse("2023-01-20T18:00:00Z"),
        numbers = listOf(1, 12, 15, 31, 47),
        bonusNumbers = listOf(9, 10),
        nextDrawTime = Instant.parse("2023-01-24T18:00:00Z"),
        jackpot = null,
    ),
)

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
@Composable
private fun DrawResultsContentPreview() {
    LottoTheme { DrawResultsScreen(DrawResultsUiState.Content(previewCards), onRetry = {}) }
}

@Preview(showBackground = true)
@Composable
private fun DrawResultsErrorPreview() {
    LottoTheme { DrawResultsScreen(DrawResultsUiState.Error(ErrorReason.NoConnection), onRetry = {}) }
}
