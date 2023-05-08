object Versions {

    const val kotlinCoroutines = "1.6.4"
    const val kotlinxSerialization = "1.4.1"
    const val ktor = "2.2.4"
    const val koinCore = "3.3.2"
    const val koinAndroid = "3.3.2"
    const val koinAndroidCompose = "3.4.1"

    const val kmpNativeCoroutinesVersion = "1.0.0-ALPHA-5"

    const val composeDesktopWeb = "1.4.0"

    const val routingCompose = "0.2.12"
    const val googleMaps = "0.3.2-alpha"

    const val junit = "4.12"
    const val mockito = "3.11.2"

    const val shadow = "7.0.0"
    const val kotlinterGradle = "3.4.5"

    const val slf4j = "2.0.7"
    const val log4j2 = "2.20.0"
    const val kermit = "1.0.0"

    const val gradleVersionsPlugin = "0.39.0"
}


object Deps {
    object Gradle {
        const val kotlinter = "org.jmailen.gradle:kotlinter-gradle:${Versions.kotlinterGradle}"
        const val shadow = "gradle.plugin.com.github.jengelman.gradle.plugins:shadow:${Versions.shadow}"
        const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
    }

    object Kotlinx {
        const val serializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val mockito = "org.mockito:mockito-inline:${Versions.mockito}"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koinCore}"
        const val test = "io.insert-koin:koin-test:${Versions.koinCore}"
        const val testJUnit4 = "io.insert-koin:koin-test-junit4:${Versions.koinCore}"
        const val android = "io.insert-koin:koin-android:${Versions.koinAndroid}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koinAndroidCompose}"
    }

    object Ktor {
        const val serverCore = "io.ktor:ktor-server-core:${Versions.ktor}"
        const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
        const val serverCors = "io.ktor:ktor-server-cors:${Versions.ktor}"
        const val serverCompression = "io.ktor:ktor-server-compression:${Versions.ktor}"
        const val serverValidation = "io.ktor:ktor-server-request-validation:${Versions.ktor}"
        const val serverStatusPages = "io.ktor:ktor-server-status-pages:${Versions.ktor}"
        const val serverCallLogging = "io.ktor:ktor-server-call-logging:${Versions.ktor}"
        const val serverDefaultHeaders = "io.ktor:ktor-server-default-headers:${Versions.ktor}"
        const val serverCachingHeaders = "io.ktor:ktor-server-caching-headers:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        const val serverContentNegotiation = "io.ktor:ktor-server-content-negotiation:${Versions.ktor}"

        const val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
        const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
        const val clientJava = "io.ktor:ktor-client-java:${Versions.ktor}"
        const val clientJs = "io.ktor:ktor-client-js:${Versions.ktor}"
    }

    object Web {
        const val routingCompose = "app.softwork:routing-compose:${Versions.routingCompose}"
        const val googleMaps = "com.github.chihsuanwu:google-maps-compose-web:${Versions.googleMaps}"
    }

    object Log {
        const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
        const val log4jslf4j = "org.apache.logging.log4j:log4j-slf4j2-impl:${Versions.log4j2}"
        const val log4j2api = "org.apache.logging.log4j:log4j-api:${Versions.log4j2}"
        const val log4j2 = "org.apache.logging.log4j:log4j-core:${Versions.log4j2}"
        const val kermit = "co.touchlab:kermit:${Versions.kermit}"
    }
}
