package com.novack.lotto_cc.feature.drawresults.domain.model

/** Outcome of a domain operation: either data or a typed [DomainError], never an exception. */
sealed interface DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Failure(val error: DomainError) : DomainResult<Nothing>
}

enum class DomainError {
    /** No connection or timeout. */
    NETWORK,

    /** The server answered with an error or an unexpected payload. */
    SERVER,

    UNKNOWN,
}
