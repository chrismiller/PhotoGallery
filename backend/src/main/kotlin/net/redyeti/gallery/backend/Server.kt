package net.redyeti.gallery.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


lateinit var appConfig: AppConfig
lateinit var appData: AppData

fun main() {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")

  appConfig = ConfigLoader().load()

  appData = initGallery(appConfig)

  embeddedServer(Netty, appConfig.port, module = Application::photoGalleryAppModule).start(wait = true)
}

fun Application.photoGalleryAppModule() {
  features()
  configureRouting(appConfig.staticImageDir, appConfig.staticGpsTrackDir)
}

fun initGallery(config: AppConfig): AppData {
  val scanner = AlbumScanner(config)
  return AppData(scanner.loadAlbums())
}
