plugins {
    alias(libs.plugins.lotto.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.novack.lotto_cc.feature.drawresults.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":feature:drawresults:domain"))

    testImplementation(testFixtures(project(":feature:drawresults:domain")))
    testImplementation(libs.okhttp.mockwebserver)
}
