plugins {
    alias(libs.plugins.lotto.android.library)
    alias(libs.plugins.lotto.koin)
}

android {
    namespace = "com.novack.lotto_cc.feature.drawresults.presentation"
}

dependencies {
    implementation(project(":feature:drawresults:domain"))
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.koin.core.viewmodel)

    testImplementation(project(":core:testing"))
    testImplementation(testFixtures(project(":feature:drawresults:domain")))
}
