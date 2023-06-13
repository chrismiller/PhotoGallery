plugins {
    id("com.github.johnrengelman.shadow") version Versions.shadow
    id("com.github.ben-manes.versions") version Versions.gradleVersionsPlugin
    id("org.jmailen.kotlinter") version Versions.kotlinterGradle
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
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.20-1.0.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
    }
}