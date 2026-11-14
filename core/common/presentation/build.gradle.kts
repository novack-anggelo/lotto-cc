plugins {
    alias(libs.plugins.lotto.android.library.compose)
    alias(libs.plugins.lotto.koin)
}

android {
    namespace = "com.novack.lotto_cc.core.common.presentation"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.koin.core.viewmodel)
    implementation(libs.koin.compose.viewmodel)

    testImplementation(project(":core:testing"))
}