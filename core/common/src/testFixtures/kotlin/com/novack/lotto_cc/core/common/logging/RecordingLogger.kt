package com.novack.lotto_cc.core.common.logging

/** [Logger] fake that records every entry for assertions. */
class RecordingLogger : Logger {

    data class Entry(val level: Level, val tag: String, val message: String, val throwable: Throwable? = null)

    enum class Level { DEBUG, INFO, WARN, ERROR }

    private val _entries = mutableListOf<Entry>()
    val entries: List<Entry> get() = _entries

    override fun d(tag: String, message: String) {
        _entries += Entry(Level.DEBUG, tag, message)
    }

    override fun i(tag: String, message: String) {
        _entries += Entry(Level.INFO, tag, message)
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        _entries += Entry(Level.WARN, tag, message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        _entries += Entry(Level.ERROR, tag, message, throwable)
    }
}
