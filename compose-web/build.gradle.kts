import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.compose)
}

repositories {
  mavenCentral()
  maven("https://maven.google.com")
}

kotlin {
  js {
    binaries.executable()
    browser()
    useCommonJs()
  }

  sourceSets {
    all {
      languageSettings {
        languageVersion = "2.0"
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(projects.common)     // enabled by TYPESAFE_PROJECT_ACCESSORS feature preview
        implementation(projects.mapLibre)   // enabled by TYPESAFE_PROJECT_ACCESSORS feature preview
        implementation(compose.html.core)
        implementation(compose.runtime)
        implementation(libs.routing.compose)
        implementation(libs.google.maps)
        implementation(libs.kotlinx.datetime)
      }
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