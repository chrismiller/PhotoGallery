import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
}

kotlin {
    jvm()

    js(IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
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

                with(Deps.SqlDelight) {
                    implementation(runtime)
                    implementation(coroutineExtensions)
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
        val commonTest by getting {
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
                implementation(Deps.SqlDelight.sqliteDriver)
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

sqldelight {
    database("PhotoGalleryDatabase") {
        packageName = "net.redyeti.gallery.db"
        sourceFolders = listOf("sqldelight")
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}
