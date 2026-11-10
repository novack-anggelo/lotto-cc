package com.novack.lotto_cc.core.common.logging

/** [Logger] fake that records every entry for assertions. Thread-safe; [entries] is a snapshot. */
class RecordingLogger : Logger {

    data class Entry(val level: Level, val tag: String, val message: String, val throwable: Throwable? = null)

    enum class Level { DEBUG, INFO, WARN, ERROR }

    private val recorded = mutableListOf<Entry>()

    val entries: List<Entry>
        get() = synchronized(recorded) { recorded.toList() }

    override fun d(tag: String, message: String) = record(Entry(Level.DEBUG, tag, message))

    override fun i(tag: String, message: String) = record(Entry(Level.INFO, tag, message))

    override fun w(tag: String, message: String, throwable: Throwable?) =
        record(Entry(Level.WARN, tag, message, throwable))

    override fun e(tag: String, message: String, throwable: Throwable?) =
        record(Entry(Level.ERROR, tag, message, throwable))

    private fun record(entry: Entry) {
        synchronized(recorded) { recorded += entry }
    }
}
