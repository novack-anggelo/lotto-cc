package com.novack.lotto_cc.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/** Shared Android + Kotlin configuration for application and library modules. */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        compileSdk = libs.version("androidCompileSdk").toInt()
        defaultConfig.minSdk = libs.version("androidMinSdk").toInt()
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        compileOptions.sourceCompatibility = javaVersion
        compileOptions.targetCompatibility = javaVersion
    }
}

/** Shared Kotlin configuration for pure JVM modules (no Android dependency). */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(libs.version("jvmTarget")))
    }
}

private val Project.javaVersion: JavaVersion
    get() = JavaVersion.toVersion(libs.version("jvmTarget"))
