package com.novack.lotto_cc.logging

import android.util.Log
import com.novack.lotto_cc.BuildConfig
import com.novack.lotto_cc.core.common.logging.Logger

/**
 * [Logger] backed by Logcat. Debug and info logs are dropped outside debug builds so release
 * builds never expose them; warnings and errors are always logged.
 * Bound to [Logger] in [com.novack.lotto_cc.di.appModule].
 */
class AndroidLogger(
    private val verbose: Boolean = BuildConfig.DEBUG,
) : Logger {
    override fun d(tag: String, message: String) {
        if (verbose) Log.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        if (verbose) Log.i(tag, message)
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        Log.w(tag, message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
