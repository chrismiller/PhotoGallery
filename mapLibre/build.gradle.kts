plugins {
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.multiplatform)
  // https://github.com/JetBrains/kotlin/tree/master/plugins/js-plain-objects
  alias(libs.plugins.js.plain.objects)
  alias(libs.plugins.turansky.seskar)
  id("maven-publish")
}

group = "net.redyeti.maplibre"

kotlin {
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
    jsMain.dependencies {
      // https://github.com/JetBrains/kotlin-wrappers/releases
      implementation(project.dependencies.platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.826"))
      implementation(kotlinWrappers.js)
      implementation(kotlin("stdlib-js"))
      implementation(libs.kotlinx.coroutines.core)
      implementation(compose.runtime)
      implementation(compose.html.core)
      // https://www.npmjs.com/package/pmtiles
      implementation(npm("pmtiles", "3.2.1"))
      // https://github.com/maplibre/maplibre-gl-js/releases
      //implementation(npm("maplibre-gl", "5.0.0-pre.6"))
      implementation(npm("maplibre-gl", "4.7.1"))
      // https://github.com/caseycesari/GeoJSON.js/releases
      implementation(npm("geojson", "0.5.0"))
    }

    jsTest.dependencies {
      implementation(kotlin("test-js"))
    }
  }
}

tasks.withType<GenerateModuleMetadata> {
  // The value 'enforced-platform' is provided in the validation error message you got
  suppressedValidationErrors.add("enforced-platform")
}
