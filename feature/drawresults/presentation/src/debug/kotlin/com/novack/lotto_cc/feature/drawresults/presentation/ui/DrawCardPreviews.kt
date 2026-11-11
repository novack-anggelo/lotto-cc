package com.novack.lotto_cc.feature.drawresults.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

@Preview(showBackground = true, backgroundColor = 0xFFF7F7F7)
@Preview(showBackground = true, backgroundColor = 0xFFF7F7F7, locale = "de")
@Composable
private fun DrawCardLotto6aus49Preview() {
    LottoTheme { DrawCard(PreviewData.lotto6aus49, Modifier.padding(16.dp)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F7F7)
@Composable
private fun DrawCardEurojackpotPreview() {
    LottoTheme { DrawCard(PreviewData.eurojackpot, Modifier.padding(16.dp)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F7F7)
@Composable
private fun DrawCardWithoutJackpotPreview() {
    LottoTheme { DrawCard(PreviewData.eurojackpot.copy(jackpot = null), Modifier.padding(16.dp)) }
}
