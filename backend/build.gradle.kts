plugins {
    kotlin("jvm")
    application
    kotlin("plugin.serialization")
}

private val staticFiles by configurations.creating {
    isCanBeConsumed = false
}

dependencies {
    implementation(projects.common)

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

    with(Deps.Log) {
        implementation(log4jslf4j)
        implementation(log4j2api)
        implementation(log4j2)
    }

    staticFiles(project(path = ":compose-web", configuration = "frontendDistribution"))

    with(Deps.Test) {
        testImplementation(mockito)
        testImplementation(junit)
    }
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