package net.redyeti.gallery.backend

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.nio.file.Path

fun Application.configureRouting(imageBase: Path, gpsBase: Path) = routing {
  route("/api") {
    api()
  }
  staticFiles("/image", imageBase.toFile())
  staticFiles("/gps", gpsBase.toFile())
  singlePageApplication {
    useResources = true
    filesPath = "app"
    defaultPage = "index.html"
  }
}

private fun Route.api() {
  get("/status") { call.respond(HttpStatusCode.OK) }

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
}