import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
    application
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    with(Deps.Kotlinx) {
        implementation(serializationCore) // JVM dependency
        implementation(coroutinesCore)
    }

    with(Deps.Ktor) {
        implementation(serverCore)
        implementation(serverNetty)
        implementation(serverCors)
        implementation(serverCompression)
        implementation(serverValidation)
        implementation(serverStatusPages)
        implementation(serverCallLogging)
        implementation(serverDefaultHeaders)
        implementation(serverCachingHeaders)
        implementation(serverContentNegotiation)
        implementation(json)
    }

    with(Deps.Test) {
        testImplementation(mockito)
        testImplementation(junit)
    }

    with(Deps.Log) {
        implementation(logback)
    }

    implementation(project(":common"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(":compose-web:assemble")
    from(File(rootProject.project("compose-web").buildDir, "distributions/")) {
        into("WEB-INF")
    }
}

application {
    mainClass.set("net.redyeti.gallery.backend.ServerKt")
}