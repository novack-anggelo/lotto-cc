import com.novack.lotto_cc.buildlogic.configureKotlinJvm
import com.novack.lotto_cc.buildlogic.configureUnitTests
import org.gradle.api.Plugin
import org.gradle.api.Project

/** Pure Kotlin/JVM module with no Android dependency: `lotto.jvm.library`. */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        configureKotlinJvm()
        configureUnitTests()
    }
}
