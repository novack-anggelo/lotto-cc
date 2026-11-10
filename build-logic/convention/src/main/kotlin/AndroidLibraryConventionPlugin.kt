import com.android.build.api.dsl.LibraryExtension
import com.novack.lotto_cc.buildlogic.configureInstrumentedTests
import com.novack.lotto_cc.buildlogic.configureKotlinAndroid
import com.novack.lotto_cc.buildlogic.configureUnitTests
import com.novack.lotto_cc.buildlogic.libs
import com.novack.lotto_cc.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android library without Compose: `lotto.android.library`. */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            lint.targetSdk = libs.version("androidTargetSdk").toInt()
        }
        configureUnitTests()
        configureInstrumentedTests()
    }
}
