package com.novack.lotto_cc.feature.drawresults.domain

import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw

class GetDrawsUseCase() {
    suspend operator fun invoke(): DomainResult<List<Draw>> {
        return DomainResult.Success(emptyList())
    }
}