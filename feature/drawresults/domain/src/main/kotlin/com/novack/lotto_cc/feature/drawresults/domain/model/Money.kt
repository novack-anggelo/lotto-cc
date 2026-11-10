package com.novack.lotto_cc.feature.drawresults.domain.model

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currencyCode: String,
)
