package net.redyeti.gallery.backend

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.io.File
import java.nio.file.Path

fun Application.configureRouting(appConfig: AppConfig) = routing {
  route("/api") {
    api(appConfig.staticGpsTrackDir)
  }

  fun staticPath(name: String, path: File) {
    println("Mapped static path: $name -> $path")
    staticFiles(name, path)
  }

  staticPath("/image", appConfig.staticImageDir.toFile())
  appConfig.otherStatic.forEach { (name, path) ->
    staticPath(name, path.toFile())
  }

  singlePageApplication {
    useResources = true
    filesPath = "app"
    defaultPage = "index.html"
  }
}

private fun Route.api(staticGpsTrackDir: Path) {
  val gpsTrackBase = staticGpsTrackDir

  get("/status") { call.respond(HttpStatusCode.OK) }

  get("/albums") {
    call.respond(appData.getAlbums())
  }

  get("/album/{key}") {
    val key = call.parameters.getOrFail<String>("key")
    val result = when(key) {
      "All" -> appData.allAlbumsMerged()
      else -> appData.getAlbum(key) ?: throw RequestValidationException(key, listOf("Invalid album ID"))
    }
    call.respond(result)
  }

  get("/gps/{key}") {
    val key = call.parameters.getOrFail<String>("key")
    val album = appData.getAlbum(key) ?: throw RequestValidationException(key, listOf("Invalid album ID"))
    if (!album.album.hasGpsTracks) throw RequestValidationException(key, listOf("Album does not have any GPS tracks"))
    call.respond(loadGpsTracks(gpsTrackBase.resolve(key)))
  }

  get("/photo/{albumKey}/{photoId}") {
    val albumKey = call.parameters.getOrFail<String>("albumKey")
    val photoId = call.parameters.getOrFail<Int>("photoId").toInt()
    val result =
      appData.getPhoto(albumKey, photoId) ?: throw RequestValidationException(photoId, listOf("Invalid photo ID"))
    call.respond(result)
  }
}