package com.novack.lotto_cc.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Enables Compose and adds the BOM-aligned UI, tooling and UI-test dependencies. */
internal fun Project.configureCompose(commonExtension: CommonExtension) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    commonExtension.buildFeatures.compose = true

    dependencies {
        val bom = platform(libs.library("androidx-compose-bom"))
        add("implementation", bom)
        add("implementation", libs.library("androidx-compose-ui"))
        add("implementation", libs.library("androidx-compose-ui-graphics"))
        add("implementation", libs.library("androidx-compose-ui-tooling-preview"))
        add("implementation", libs.library("androidx-compose-material3"))
        add("debugImplementation", libs.library("androidx-compose-ui-tooling"))
        add("debugImplementation", libs.library("androidx-compose-ui-test-manifest"))
        add("androidTestImplementation", bom)
        add("androidTestImplementation", libs.library("androidx-compose-ui-test-junit4"))
    }
}
