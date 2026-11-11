package com.novack.lotto_cc.core.designsystem.theme

data class LottoCardUiModel(
    val title: String,
    val date: String,
    val numbers: List<String>,
    val additionalNumbers: List<String>,
    val nextDrawDate: String,
    val jackpotValue: String,
    val additionalGames: List<String>? = null,
)
