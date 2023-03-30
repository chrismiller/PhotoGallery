# Photo Gallery

![kotlin-version](https://img.shields.io/badge/kotlin-1.8.0-orange)

**Kotlin Multiplatform** project with Compose for Web UI, and Ktor backend.
* Web (Compose for Web)
* JVM (small Ktor back end service + `Main.kt` in `common` module)

### Building
To run backend you can either run `./gradlew :backend:run` or run `Server.kt` directly. After doing that you should then for example be able to open http://localhost:8080/albums in a browser.

### Compose for Web client

The Compose for Web client resides in the `compose-web` module and can be run by
invoking `./gradlew :compose-web:jsBrowserDevelopmentRun`

### Backend code

Using shadowJar plugin to create an "uber" jar as shown below.

`./gradlew :backend:shadowJar`

### Languages, libraries and tools used

* [Kotlin](https://kotlinlang.org/)
* [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
* [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
* [Ktor client library](https://github.com/ktorio/ktor)
* [Koin](https://github.com/InsertKoinIO/koin)
* [KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)
