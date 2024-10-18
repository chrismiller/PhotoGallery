package net.redyeti.gallery.backend

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.features() {
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

  install(Compression)
  install(RequestValidation)

  install(StatusPages) {
    exception<RequestValidationException> { call, cause ->
      call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
    }
  }

  install(CallLogging) {
    val formatter = LogFormatter()
    format {
      formatter.format(it)
    }
  }
  install(DefaultHeaders)
  install(CachingHeaders)
}