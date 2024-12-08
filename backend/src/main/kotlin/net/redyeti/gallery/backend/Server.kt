package net.redyeti.gallery.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

const val GALLERY_CONFIG = "GALLERY_CONFIG"
const val DEFAULT_PATH = "D:/Photos/PhotoGallery/config.properties"

lateinit var appConfig: AppConfig
lateinit var appData: AppData

fun main() {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")

  val configFile = System.getenv(GALLERY_CONFIG) ?: DEFAULT_PATH
  appConfig = ConfigLoader().load(configFile)

  appData = initGallery(appConfig)

  embeddedServer(Netty, appConfig.port, module = Application::photoGalleryAppModule).start(wait = true)
}

fun Application.photoGalleryAppModule() {
  features()
  configureRouting(appConfig)
}

fun initGallery(config: AppConfig): AppData {
  val scanner = AlbumScanner(config)
  return AppData(scanner.loadAlbums())
}
