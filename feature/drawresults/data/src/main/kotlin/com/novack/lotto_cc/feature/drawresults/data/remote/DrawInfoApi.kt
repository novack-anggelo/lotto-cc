package com.novack.lotto_cc.feature.drawresults.data.remote

import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DrawInfoApi {

    /** @param lotteries comma-separated lottery ids, e.g. `6aus49,eurojackpot` (see [LotteryIds]). */
    @GET("v1/drawinfo/aggregated/{lotteries}")
    suspend fun getAggregatedDrawInfo(
        @Path("lotteries") lotteries: String,
    ): List<LotteryDrawInfoDto>
}
