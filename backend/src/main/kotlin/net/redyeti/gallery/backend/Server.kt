package net.redyeti.gallery.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.PhotoGalleryApi


private const val BASE_DIR = "F:/PhotoGallery"
lateinit var appConfig: AppConfig
lateinit var appData: AppData

fun main() {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")

  val koin = initKoin(enableNetworkLogs = true).koin
  val photoGalleryApi = koin.get<PhotoGalleryApi>()

  appConfig = ConfigLoader().load()

  photoGalleryApi.baseUrl = "http://chrisdesktop:${appConfig.port}/api"

  appData = initGallery(appConfig)

  embeddedServer(Netty, appConfig.port, module = Application::photoGalleryAppModule).start(wait = true)
}

fun Application.photoGalleryAppModule() {
  features()
  configureRouting(appConfig.staticwebDir)
}

fun initGallery(config: AppConfig): AppData {
  val scanner = AlbumScanner(config)
  return AppData(scanner.loadAlbums())
}
