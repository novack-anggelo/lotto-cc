plugins {
    alias(libs.plugins.lotto.android.application)
    alias(libs.plugins.lotto.koin)
}

android {
    namespace = "com.novack.lotto_cc"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.novack.lotto_cc"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":feature:drawresults:data"))
    implementation(project(":feature:drawresults:presentation"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.androidx.core.ktx)
}
