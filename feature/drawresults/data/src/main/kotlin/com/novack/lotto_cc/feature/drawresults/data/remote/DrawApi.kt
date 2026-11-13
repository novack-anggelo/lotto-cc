package com.novack.lotto_cc.feature.drawresults.data.remote

import com.novack.lotto_cc.feature.drawresults.data.remote.dto.LotteryDrawInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface DrawApi {

    @GET("v1/drawinfo/aggregated/{lotteries}")
    suspend fun getAggregatedDrawInfo(
        @Path("lotteries") lotteries: String,
    ): List<LotteryDrawInfo>
}