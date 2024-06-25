pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

rootProject.name = "PhotoGallery"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("common")
include("compose-web")
include("backend")
