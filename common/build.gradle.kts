plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlinx.serialization)
}

kotlin {
  jvm()

  js {
    browser()
    binaries.executable()
  }

  sourceSets {
    all {
      languageSettings {
        languageVersion = "2.0"
      }
    }

    commonMain {
      dependencies {
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.json)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialisation.core)
        api(libs.koin.core)
        api(libs.kermit)
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
        implementation(libs.koin.test)
        implementation(libs.kotlinx.coroutines.test)
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(libs.ktor.client.java)
        implementation(libs.slf4j.api)
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(libs.ktor.client.js)
      }
    }
  }
}
