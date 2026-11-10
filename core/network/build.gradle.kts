plugins {
    alias(libs.plugins.lotto.android.library)
    alias(libs.plugins.lotto.koin)
}

android {
    namespace = "com.novack.lotto_cc.core.network"

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://www.lotto24.de/public/\"")
    }
}

dependencies {
    api(libs.retrofit)
    api(libs.okhttp)
    api(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
}
