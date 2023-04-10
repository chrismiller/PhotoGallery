package net.redyeti.gallery.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.PhotoGalleryApi
import java.io.FileInputStream
import java.util.*
import kotlin.io.path.Path


private const val BASE_DIR = "F:/PhotoGallery"
lateinit var appConfig: AppConfig
lateinit var appData: AppData

fun main() {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")

  val koin = initKoin(enableNetworkLogs = true).koin
  val photoGalleryApi = koin.get<PhotoGalleryApi>()

  val props = Properties()
  FileInputStream("$BASE_DIR/config.properties").use {
    props.load(it)
  }

  appConfig = AppConfig(
    Path(props.getProperty("EXIFTOOL", "exiftool.exe")),
    Path(props.getProperty("IMAGEMAGICK", "imagemagick.exe")),
    Path(props.getProperty("ALBUMS_DIR", "$BASE_DIR/Albums")),
    props.getProperty("HTTP_PORT", "8081").toInt()
  )
  photoGalleryApi.baseUrl = "http://localhost:${appConfig.port}/api"

  appData = initGallery(appConfig)

  embeddedServer(Netty, appConfig.port, module = Application::photoGalleryAppModule).start(wait = true)
}

fun Application.photoGalleryAppModule() {
  features()
  configureRouting()
}

fun initGallery(config: AppConfig): AppData {
  val scanner = AlbumScanner(config)
  return AppData(scanner.loadAlbums())
}
