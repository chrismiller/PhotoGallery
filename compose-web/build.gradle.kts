import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.multiplatform)
  // https://github.com/JetBrains/kotlin/tree/master/plugins/js-plain-objects
  alias(libs.plugins.js.plain.objects)
  alias(libs.plugins.turansky.seskar)
}

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
      implementation(project.dependencies.platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.818"))
      implementation(kotlinWrappers.js)
      implementation(kotlin("stdlib-js"))
      implementation(compose.runtime)
      implementation(compose.html.core)
      implementation(libs.routing.compose)
      implementation(projects.common)     // enabled by TYPESAFE_PROJECT_ACCESSORS feature preview
      implementation(projects.mapLibre)   // enabled by TYPESAFE_PROJECT_ACCESSORS feature preview
      implementation(libs.google.maps)
      implementation(libs.kotlinx.datetime)
    }

    jsTest.dependencies {
      implementation(kotlin("test-js"))
    }
  }
}

// Substitute params into index.html
tasks.named<ProcessResources>("jsProcessResources") {
  val webpack = project.tasks.withType(KotlinWebpack::class).first()

  val bundleFile = webpack.mainOutputFileName
  val publicPath = "./" // TODO get public path from webpack config

  filesMatching("*.html") {
    expand("bundle" to bundleFile.get(), "publicPath" to publicPath)
  }
}

private val frontendDistribution by configurations.creating {
  isCanBeConsumed = true
  isCanBeResolved = false
}

artifacts {
  add(frontendDistribution.name, tasks.named("jsBrowserDistribution"))
}
