package com.novack.lotto_cc.core.domain.models

sealed interface DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Failure(
        val error: DomainError,
        val message: String? = null,
        val cause: Throwable? = null
    ) : DomainResult<Nothing>
}

enum class DomainError {
    NETWORK,
    SERVER,
    UNKNOWN,
}