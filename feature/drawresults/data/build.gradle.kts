plugins {
    alias(libs.plugins.lotto.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.lotto.koin)
}

android {
    namespace = "com.novack.lotto_cc.feature.drawresults.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:common:domain"))
    implementation(project(":feature:drawresults:domain"))

    testImplementation(libs.okhttp.mockwebserver)
}
