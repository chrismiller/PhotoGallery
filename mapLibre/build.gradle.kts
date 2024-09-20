plugins {
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.multiplatform)
  // id("io.github.turansky.seskar") version Versions.seskar
  id("maven-publish")
}

group = "net.redyeti.maplibre"

fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target"

kotlin {
  jvmToolchain(21)

  js {
    binaries.executable()
    browser {
      testTask {
        testLogging.showStandardStreams = true
        useKarma {
          useChromeHeadless()
          useFirefox()
        }
      }
    }
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(project.dependencies.enforcedPlatform(kotlinw("wrappers-bom:1.0.0-pre.810")))
        implementation(kotlinw("js"))
        implementation(kotlin("stdlib-js"))

        implementation(compose.html.core)
        implementation(compose.runtime)
        implementation(npm("pmtiles", "3.1.0"))
        implementation(npm("maplibre-gl", "4.7.0"))

        // implementation(Deps.seskar)
      }
    }
    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}

tasks.withType<GenerateModuleMetadata> {
  // The value 'enforced-platform' is provided in the validation
  // error message you got
  suppressedValidationErrors.add("enforced-platform")
}
