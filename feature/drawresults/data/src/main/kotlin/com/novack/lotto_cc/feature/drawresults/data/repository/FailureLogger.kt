package com.novack.lotto_cc.feature.drawresults.data.repository

import android.util.Log

/** Records why a request failed; [DomainResult][com.novack.lotto_cc.feature.drawresults.domain.model.DomainResult] only carries the category. */
internal fun interface FailureLogger {
    fun log(message: String, cause: Throwable?)
}

internal class AndroidFailureLogger : FailureLogger {
    override fun log(message: String, cause: Throwable?) {
        Log.w("DrawResultsRepository", message, cause)
    }
}
