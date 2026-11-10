plugins {
    alias(libs.plugins.lotto.jvm.library)
}

dependencies {
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
