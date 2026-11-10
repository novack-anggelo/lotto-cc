import com.novack.lotto_cc.buildlogic.libs
import com.novack.lotto_cc.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Koin with the Koin Compiler Plugin (compile-time checked graph): `lotto.koin`.
 * Adds `koin-core` through the BOM; modules add platform artifacts (android, compose) themselves.
 * Works in any plugin order: dependencies are added once a Kotlin JVM or Android plugin is applied.
 */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("io.insert-koin.compiler.plugin")

        listOf("org.jetbrains.kotlin.jvm", "com.android.base").forEach { baseId ->
            pluginManager.withPlugin(baseId) {
                dependencies {
                    add("implementation", platform(libs.library("koin-bom")))
                    add("implementation", libs.library("koin-core"))
                }
            }
        }
    }
}
