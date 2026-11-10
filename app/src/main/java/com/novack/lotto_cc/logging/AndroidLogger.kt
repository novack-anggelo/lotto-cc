package com.novack.lotto_cc.logging

import android.util.Log
import com.novack.lotto_cc.core.common.logging.Logger

/** [Logger] backed by Logcat. Registered in the app's Koin root module. */
class AndroidLogger : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        Log.w(tag, message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
