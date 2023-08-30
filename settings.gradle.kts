pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

rootProject.name = "PhotoGallery"

include("common")
include("compose-web")
include("backend")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
