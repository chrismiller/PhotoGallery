import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  // https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.compose) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.kotlinx.serialization) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.johnrengelman.shadow) apply false
  alias(libs.plugins.kotlinter) apply false
}

buildscript {
  val kotlinVersion: String by project
  println(kotlinVersion)

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
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  val jvmTarget = JvmTarget.JVM_21
  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = jvmTarget.target
    targetCompatibility = jvmTarget.target
  }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(jvmTarget)
  }
}
