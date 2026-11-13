package com.novack.lotto_cc.feature.drawresults.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LotteryDrawInfo(
    @SerialName("lottery")
    val lottery: String,
    @SerialName("lastDraw")
    val lastDraw: LastDraw,
    @SerialName("nextDraw")
    val nextDraw: NextDraw
) {
    @Serializable
    data class LastDraw(
        @SerialName("drawIdentifier")
        val id: String,
        @SerialName("drawDateUtc")
        val drawDateUtc: String,
        @SerialName("drawResult")
        val drawResult: DrawResult
    ) {
        @Serializable
        data class DrawResult(
            @SerialName("numbers")
            val numbers: List<Int>,
            @SerialName("superNumber")
            val superNumber: Int? = null,
            @SerialName("euroNumbers")
            val euroNumbers: List<Int>? = null,
            @SerialName("number")
            val additionalGameNumber: String? = null,
        )
    }

    @Serializable
    data class NextDraw(
        @SerialName("drawIdentifier")
        val id: String,
        @SerialName("drawDateUtc")
        val drawDateUtc: String,
        @SerialName("jackpot")
        val jackpot: Jackpot? = null
    ) {
        @Serializable
        data class Jackpot(
            @SerialName("currency")
            val currency: String,
            @SerialName("jackpots")
            val jackpots: Map<String, String?>?
        )
    }
}