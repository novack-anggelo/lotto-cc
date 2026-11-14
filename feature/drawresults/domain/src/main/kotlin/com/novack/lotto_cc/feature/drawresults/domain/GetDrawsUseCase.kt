package com.novack.lotto_cc.feature.drawresults.domain

import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw
import com.novack.lotto_cc.feature.drawresults.domain.repository.DrawRepository

class GetDrawsUseCase(private val repository: DrawRepository) {
    suspend operator fun invoke(): DomainResult<List<Draw>> {
        return repository.getDraws()
    }
}