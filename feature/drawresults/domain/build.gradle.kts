plugins {
    alias(libs.plugins.lotto.jvm.library)
}

dependencies {
    implementation(project(":core:common:domain"))
    implementation(libs.kotlinx.coroutines.core)
}
