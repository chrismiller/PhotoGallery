plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version Versions.composeDesktopWeb
}

version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.common)     // enabled by TYPESAFE_PROJECT_ACCESSORS feature preview
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(Deps.Web.routingCompose)
                implementation(Deps.Web.googleMaps)
                implementation(Deps.Kotlinx.dateTime)
            }
        }
    }
    jvmToolchain(19)
}