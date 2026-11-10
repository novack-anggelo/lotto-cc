import com.android.build.api.dsl.ApplicationExtension
import com.novack.lotto_cc.buildlogic.configureCompose
import com.novack.lotto_cc.buildlogic.configureInstrumentedTests
import com.novack.lotto_cc.buildlogic.configureKotlinAndroid
import com.novack.lotto_cc.buildlogic.configureUnitTests
import com.novack.lotto_cc.buildlogic.libs
import com.novack.lotto_cc.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android application with Compose: `lotto.android.application`. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureCompose(this)
            defaultConfig {
                targetSdk = libs.version("androidTargetSdk").toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
        configureUnitTests()
        configureInstrumentedTests()
    }
}
