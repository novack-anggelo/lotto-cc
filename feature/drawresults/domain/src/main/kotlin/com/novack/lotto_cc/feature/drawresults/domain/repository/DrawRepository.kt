package com.novack.lotto_cc.feature.drawresults.domain.repository

import com.novack.lotto_cc.core.domain.models.DomainResult
import com.novack.lotto_cc.feature.drawresults.domain.models.Draw

interface DrawRepository {
    suspend fun getDraws(): DomainResult<List<Draw>>
}