import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

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
      implementation(project.dependencies.platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.815"))
      implementation(kotlinWrappers.js)
      implementation(kotlin("stdlib-js"))
      implementation(compose.runtime)
      implementation(compose.html.core)
      implementation(npm("pmtiles", "3.1.0"))
      implementation(npm("maplibre-gl", "4.7.1"))
    }

    jsTest.dependencies {
      implementation(kotlin("test-js"))
    }
  }
}

tasks.withType<GenerateModuleMetadata> {
  // The value 'enforced-platform' is provided in the validation
  // error message you got
  suppressedValidationErrors.add("enforced-platform")
}

tasks.withType<Kotlin2JsCompile>().configureEach {
  compilerOptions {
    target = "es2015"
  }
}
