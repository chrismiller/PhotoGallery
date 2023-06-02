import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
    application
    kotlin("plugin.serialization")
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
        implementation(log4jslf4j)
        implementation(log4j2api)
        implementation(log4j2)
    }

    implementation(project(":common"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(":compose-web:jsBrowserDevelopmentExecutableDistribution")
    from(File(rootProject.project("compose-web").buildDir, "developmentExecutable/")) {
        into("app")
    }
//    dependsOn(":compose-web:assemble")
//    from(File(rootProject.project("compose-web").buildDir, "distributions/")) {
//        into("app")
//    }
}

application {
    mainClass.set("net.redyeti.gallery.backend.ServerKt")
}