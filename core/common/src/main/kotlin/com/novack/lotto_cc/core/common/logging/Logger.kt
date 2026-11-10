package com.novack.lotto_cc.core.common.logging

/**
 * Platform-independent logging. Injected wherever a class logs, so tests can use a fake
 * (see `RecordingLogger` in this module's test fixtures). The Android implementation lives in `:app`.
 */
interface Logger {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}
