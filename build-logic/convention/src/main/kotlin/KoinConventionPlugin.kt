import com.novack.lotto_cc.buildlogic.libs
import com.novack.lotto_cc.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Koin with the Koin Compiler Plugin (compile-time checked graph): `lotto.koin`.
 * Adds `koin-core` through the BOM; modules add platform artifacts (android, compose) themselves.
 */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("io.insert-koin.compiler.plugin")

        dependencies {
            val bom = platform(libs.library("koin-bom"))
            add("implementation", bom)
            add("implementation", libs.library("koin-core"))
            add("testImplementation", bom)
            add("testImplementation", libs.library("koin-test"))
        }
    }
}
