plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlinx.serialization)
  application
  alias(libs.plugins.johnrengelman.shadow)
}

private val staticFiles by configurations.creating {
  isCanBeConsumed = false
}

dependencies {
  implementation(projects.common)

  implementation(libs.kotlinx.serialisation.core) // JVM dependency
  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.ktor.server.core)
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.server.cors)
  implementation(libs.ktor.server.compression)
  implementation(libs.ktor.server.request.validation)
  implementation(libs.ktor.server.status.pages)
  implementation(libs.ktor.server.call.logging)
  implementation(libs.ktor.server.default.headers)
  implementation(libs.ktor.server.caching.headers)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.server.request.validation)
  implementation(libs.ktor.server.status.pages)
  implementation(libs.ktor.server.call.logging)
  implementation(libs.ktor.server.default.headers)
  implementation(libs.ktor.server.caching.headers)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)

  implementation(libs.solar.positioning)
  implementation(libs.jansi)

  implementation(libs.log4j.slf4j2.impl)
  implementation(libs.log4j.api)
  implementation(libs.log4j.core)

  staticFiles(project(path = ":compose-web", configuration = "frontendDistribution"))

  testImplementation(kotlin("test"))
  testImplementation(libs.mockito)
  testImplementation(libs.junit)
}

tasks.processResources {
  from(staticFiles) {
    include("**/*")
    into("app")
  }
}

application {
  mainClass.set("net.redyeti.gallery.backend.ServerKt")
}