plugins {
    alias(libs.plugins.lotto.android.library)
}

android {
    namespace = "com.novack.lotto_cc.feature.drawresults.presentation"
}

dependencies {
    implementation(project(":feature:drawresults:domain"))
    implementation(libs.androidx.lifecycle.viewmodel)

    testImplementation(project(":core:testing"))
    testImplementation(testFixtures(project(":feature:drawresults:domain")))
}
