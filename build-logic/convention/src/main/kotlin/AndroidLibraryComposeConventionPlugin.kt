import com.android.build.api.dsl.LibraryExtension
import com.novack.lotto_cc.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android library with Compose: `lotto.android.library.compose`. */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("lotto.android.library")

        extensions.configure<LibraryExtension> {
            configureCompose(this)
        }
    }
}
