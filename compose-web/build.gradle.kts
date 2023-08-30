import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose") version Versions.composeDesktopWeb
}

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
  js {
    binaries.executable()
    browser()
    useCommonJs()
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