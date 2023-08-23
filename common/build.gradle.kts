plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("com.google.devtools.ksp")
}

kotlin {
  jvm()

  js {
    browser()
  }

  sourceSets {
    commonMain {
      dependencies {
        with(Deps.Ktor) {
          implementation(clientCore)
          implementation(clientJson)
          implementation(clientLogging)
          implementation(contentNegotiation)
          implementation(json)
        }

        with(Deps.Kotlinx) {
          implementation(coroutinesCore)
          implementation(serializationCore)
        }

        with(Deps.Koin) {
          api(core)
          api(test)
        }

        with(Deps.Log) {
          api(kermit)
        }
      }
    }
    commonTest {
      dependencies {
        implementation(Deps.Koin.test)
        implementation(Deps.Kotlinx.coroutinesTest)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation(Deps.Ktor.clientJava)
        implementation(Deps.Log.slf4j)
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(Deps.Ktor.clientJs)
      }
    }
  }
}
