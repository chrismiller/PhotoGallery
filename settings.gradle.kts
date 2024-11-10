pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    // For dev Kotlin builds
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
  }
}

rootProject.name = "PhotoGallery"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }

  versionCatalogs {
    create("kotlinWrappers") {
      // https://github.com/JetBrains/kotlin-wrappers/releases
      val wrappersVersion = "0.0.1-pre.826"
      from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
    }
  }
}

include("common")
include("backend")
include("mapLibre")
include("compose-web")
