import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.github.johnrengelman.shadow") version Versions.shadow
  id("com.github.ben-manes.versions") version Versions.gradleVersionsPlugin
  id("org.jmailen.kotlinter") version Versions.kotlinterGradle
  // https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
  id("org.jetbrains.compose") version Versions.composeVersion apply false
}

buildscript {
  val kotlinVersion: String by project
  println(kotlinVersion)

  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }

  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
    classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Versions.kspGradlePlugin}")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
    maven(url = "https://jitpack.io")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "21"
}