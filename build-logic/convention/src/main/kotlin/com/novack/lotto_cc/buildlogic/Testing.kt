package com.novack.lotto_cc.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

/** JVM unit-test dependencies shared by every module. */
internal fun Project.configureUnitTests() {
    dependencies {
        add("testImplementation", kotlin("test"))
        add("testImplementation", libs.library("junit"))
        add("testImplementation", libs.library("kotlinx-coroutines-test"))
        add("testImplementation", libs.library("turbine"))
    }
}

/** Instrumented-test dependencies shared by Android modules. */
internal fun Project.configureInstrumentedTests() {
    dependencies {
        add("androidTestImplementation", libs.library("androidx-test-ext-junit"))
        add("androidTestImplementation", libs.library("androidx-test-espresso-core"))
    }
}
