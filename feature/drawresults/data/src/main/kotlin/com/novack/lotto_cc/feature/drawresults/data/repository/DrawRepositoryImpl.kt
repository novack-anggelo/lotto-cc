package com.novack.lotto_cc.feature.drawresults.data.repository

import com.novack.lotto_cc.core.domain.models.DomainError
import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.core.domain.models.DomainResult.Failure
import com.novack.lotto_cc.feature.drawresults.data.mappers.toDomain
import com.novack.lotto_cc.feature.drawresults.data.remote.DrawApi
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawRepository
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

private const val SUPPORTED_LOTTERIES = "6aus49,eurojackpot,spiel77,super6"

class DrawRepositoryImpl(private val api: DrawApi) : DrawRepository {
    override suspend fun getDraws(): DomainResult<List<Draw>> {
        return try {
            val draws = api.getAggregatedDrawInfo(SUPPORTED_LOTTERIES).toDomain()
            if (draws.isEmpty()) Failure(DomainError.SERVER, message = "Response contains no draws")
            else DomainResult.Success(draws)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Failure(DomainError.NETWORK, "Network error", e)
        } catch (e: HttpException) {
            Failure(DomainError.SERVER, "HTTP ${e.code()}", e)
        } catch (e: SerializationException) {
            Failure(DomainError.SERVER, "Malformed response", e)
        } catch (e: Exception) {
            Failure(DomainError.UNKNOWN, "Unexpected error", e)
        }

    }
}