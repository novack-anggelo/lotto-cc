package com.novack.lotto_cc.feature.drawresults.data.remote

internal fun readResource(name: String): String =
    requireNotNull(object {}.javaClass.classLoader?.getResource(name)) { "Missing test resource $name" }.readText()
