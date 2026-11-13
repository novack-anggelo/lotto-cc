package com.novack.lotto_cc.feature.drawresults.domain.models

sealed interface AdditionalGame {
    val number: String
    data class Spiel77(override val number: String) : AdditionalGame
    data class Super6(override val number: String) : AdditionalGame
}