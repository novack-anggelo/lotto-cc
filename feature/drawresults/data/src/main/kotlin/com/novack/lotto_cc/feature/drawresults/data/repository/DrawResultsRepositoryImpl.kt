package com.novack.lotto_cc.feature.drawresults.data.repository

import com.novack.lotto_cc.core.common.logging.Logger
import com.novack.lotto_cc.feature.drawresults.data.mapper.toDomain
import com.novack.lotto_cc.feature.drawresults.data.remote.DrawInfoApi
import com.novack.lotto_cc.feature.drawresults.data.remote.LotteryIds
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainError
import com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.model.LotteryDraw
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawResultsRepository
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "DrawResultsRepository"
private val REQUESTED_LOTTERIES = LotteryIds.SUPPORTED.joinToString(",")

/**
 * Network-only repository. Retrofit suspend calls are main-safe (OkHttp performs the call and
 * parsing on its own threads), so no dispatcher switch is needed here.
 * Exceptions never leave this class: they become a [DomainError].
 */
internal class DrawResultsRepositoryImpl(
    private val api: DrawInfoApi,
    private val logger: Logger,
) : DrawResultsRepository {

    override suspend fun getDrawResults(): DomainResult<List<LotteryDraw>> =
        try {
            val draws = api.getAggregatedDrawInfo(REQUESTED_LOTTERIES).toDomain()
            if (draws.isEmpty()) failure(DomainError.SERVER, "Response contained no usable draws")
            else DomainResult.Success(draws)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            failure(DomainError.NETWORK, "Network error", e)
        } catch (e: HttpException) {
            failure(DomainError.SERVER, "HTTP ${e.code()}", e)
        } catch (e: SerializationException) {
            failure(DomainError.SERVER, "Malformed response", e)
        } catch (e: Exception) {
            failure(DomainError.UNKNOWN, "Unexpected error", e)
        }

    private fun failure(error: DomainError, message: String, cause: Throwable? = null): DomainResult.Failure {
        logger.w(TAG, message, cause)
        return DomainResult.Failure(error)
    }
}
