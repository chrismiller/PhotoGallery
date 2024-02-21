plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
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
        implementation(project.dependencies.enforcedPlatform(kotlinw("wrappers-bom:${Versions.kotlinWrappers}")))
        implementation(kotlinw("js"))
        implementation(kotlin("stdlib-js"))

        implementation(compose.html.core)
        implementation(compose.runtime)
        implementation(npm("pmtiles", Deps.Npm.pmTiles))
        implementation(npm("maplibre-gl", Deps.Npm.mapLibreGl))

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
