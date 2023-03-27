package net.redyeti.gallery.backend

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.PhotoGalleryApi
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.io.path.Path


private const val BASE_DIR = "F:/PhotoGallery"
lateinit var appConfig: AppConfig
lateinit var appData: AppData

fun main() {
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
  photoGalleryApi.baseUrl = "http://localhost:${appConfig.port}"

  appData = initGallery(appConfig)

  embeddedServer(Netty, appConfig.port, module = Application::photoGalleryAppModule).start(wait = true)
}

fun Application.photoGalleryAppModule() {
  install(ContentNegotiation) {
    json()
  }

  install(CORS) {
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Delete)
    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.AccessControlAllowOrigin)
    // header("any header") if you want to add any header
    allowCredentials = true
    allowNonSimpleContentTypes = true
    anyHost()
  }

  install(Compression) {
    gzip {
//      condition {
//        request.headers[HttpHeaders.Referrer]?.contains("redyeti.net") == true
//      }
    }
    deflate {
//      condition {
//        request.headers[HttpHeaders.Referrer]?.startsWith("https://redyeti.net/") == true
//      }
    }
  }

  install(RequestValidation) {
  }

  install(StatusPages) {
    exception<RequestValidationException> { call, cause ->
      call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
    }
  }

  configureRouting(appData)
}

fun Application.configureRouting(appData: AppData) {
  routing {
    get("/") {
      call.respondText(
        this::class.java.classLoader.getResource("index.html")!!.readText(),
        ContentType.Text.Html
      )
    }
    static("/") {
      resources("")
    }

    get("/albums") {
      call.respond(appData.getAlbums())
    }

    get("/album/{id}") {
      val id = call.parameters.getOrFail<Int>("id").toInt()
      val result = appData.getAlbum(id) ?: throw RequestValidationException(id, listOf("Invalid album ID"))
      call.respond(result)
    }

    get("/photo/{albumId}/{photoId}") {
      val albumId = call.parameters.getOrFail<Int>("albumId").toInt()
      val photoId = call.parameters.getOrFail<Int>("photoId").toInt()
      val result = appData.getPhoto(albumId, photoId) ?: throw RequestValidationException(photoId, listOf("Invalid photo ID"))
      call.respond(result)
    }

    static("/image") {
      staticRootFolder = File("F:\\PhotoGallery\\Albums")
      files(".")
    }
  }
}

fun initGallery(config: AppConfig): AppData {
  val scanner = AlbumScanner(config)
  return AppData(scanner.loadAlbums())
}
