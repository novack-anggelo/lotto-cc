plugins {
    alias(libs.plugins.lotto.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.lotto.koin)
}

android {
    namespace = "com.novack.lotto_cc.feature.drawresults.data"

    // android.util.Log is a no-op in JVM unit tests instead of throwing "not mocked".
    testOptions.unitTests.isReturnDefaultValues = true
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":feature:drawresults:domain"))

    testImplementation(testFixtures(project(":feature:drawresults:domain")))
    testImplementation(libs.okhttp.mockwebserver)
}
