import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.compose) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.kotlinx.serialization) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.johnrengelman.shadow) apply false
  //alias(libs.plugins.kotlinter) apply false
}

buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

subprojects {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
    maven(url = "https://jitpack.io")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
    // maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    // For dev Kotlin builds
    maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
  }

  val jvmTarget = JvmTarget.JVM_21
  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = jvmTarget.target
    targetCompatibility = jvmTarget.target
  }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(jvmTarget)
    compilerOptions {
      apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
      languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
    }
  }

  tasks.withType<Kotlin2JsCompile>().configureEach {
    compilerOptions {
      target = "es2015"
      freeCompilerArgs.addAll(
        "-Xdont-warn-on-error-suppression",
        "-Xir-generate-inline-anonymous-functions",
        // TODO: remove after migration on Kotlin `2.1.0`
        "-XXLanguage:+JsAllowInvalidCharsIdentifiersEscaping",
      )
    }
  }
}
