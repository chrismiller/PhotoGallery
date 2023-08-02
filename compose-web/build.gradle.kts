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
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(Deps.Web.routingCompose)
                implementation(Deps.Web.googleMaps)
                implementation(project(":common"))
            }
        }
    }
    jvmToolchain(19)
}