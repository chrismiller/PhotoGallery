object Versions {

    const val kotlinCoroutines = "1.6.4"
    const val kotlinxSerialization = "1.4.1"
    const val ktor = "2.2.4"
    const val koinCore = "3.3.2"
    const val koinAndroid = "3.3.2"
    const val koinAndroidCompose = "3.4.1"

    const val kmpNativeCoroutinesVersion = "1.0.0-ALPHA-4"

    const val compose = "1.4.0-alpha03"
    const val navCompose = "2.5.2"

    const val composeDesktopWeb = "1.3.0"

    const val junit = "4.12"
    const val testCore = "1.3.0"
    const val mockito = "3.11.2"
    const val robolectric = "4.6.1"

    const val sqlDelight = "1.5.5"
    const val shadow = "7.0.0"
    const val kotlinterGradle = "3.4.5"

    const val slf4j = "1.7.30"
    const val logback = "1.2.3"
    const val kermit = "1.0.0"

    const val gradleVersionsPlugin = "0.39.0"
}


object Deps {
    object Gradle {
        const val kotlinter = "org.jmailen.gradle:kotlinter-gradle:${Versions.kotlinterGradle}"
        const val shadow = "gradle.plugin.com.github.jengelman.gradle.plugins:shadow:${Versions.shadow}"
        const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
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
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
        const val testCore = "androidx.test:core:${Versions.testCore}"

        const val composeUiTest = "androidx.compose.ui:ui-test:${Versions.compose}"
        const val composeUiTestJUnit = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
        const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
        const val composeNavTesting = "androidx.navigation:navigation-testing:${Versions.navCompose}"
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

    object SqlDelight {
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val coroutineExtensions = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
        const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"
    }

    object Log {
        const val slf4j = "org.slf4j:slf4j-simple:${Versions.slf4j}"
        const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"
        const val kermit = "co.touchlab:kermit:${Versions.kermit}"
    }

    object Misc {
    }
}
