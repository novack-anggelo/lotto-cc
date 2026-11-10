package com.novack.lotto_cc.feature.drawresults.data.remote

/** Lottery identifiers used by the draw info API. */
object LotteryIds {
    const val LOTTO_6AUS49 = "6aus49"
    const val EUROJACKPOT = "eurojackpot"

    /** Lotteries requested from the API; each needs a matching branch in the DTO mappers. */
    val SUPPORTED = listOf(LOTTO_6AUS49, EUROJACKPOT)
}
