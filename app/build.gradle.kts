plugins {
    alias(libs.plugins.lotto.android.application)
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
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
