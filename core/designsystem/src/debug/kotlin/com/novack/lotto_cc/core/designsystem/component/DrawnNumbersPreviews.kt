package com.novack.lotto_cc.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.novack.lotto_cc.core.designsystem.theme.LottoTheme

@Preview
@Composable
private fun DrawnNumbersLotto6aus49Preview() {
    LottoTheme {
        DrawnNumbers(listOf(1, 2, 3, 14, 25, 36), listOf(4))
    }
}

@Preview
@Composable
private fun DrawnNumbersEurojackpotPreview() {
    LottoTheme {
        DrawnNumbers(listOf(1, 2, 3, 14, 25), listOf(36, 42))
    }
}
