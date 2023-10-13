package net.redyeti.gallery.backend

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.util.date.*
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole

class LogFormatter {
  private var clock: () -> Long = { getTimeMillis() }
  private var isColorsEnabled: Boolean = true

  internal fun format(call: ApplicationCall): String {
    return when (val status = call.response.status() ?: "Unhandled") {
      HttpStatusCode.Found -> "${colored(status as HttpStatusCode)}: " +
          "${call.request.toLogStringWithColors()} -> ${call.response.headers[HttpHeaders.Location]}"

      "Unhandled" -> "${colored(status, Ansi.Color.RED)}: ${call.request.toLogStringWithColors()}"
      else -> "${colored(status as HttpStatusCode)}: ${call.request.toLogStringWithColors()}"
    }
  }

  internal fun ApplicationRequest.toLogStringWithColors(): String {
    val ip = call.request.headers["cf-connecting-ip"] ?: call.request.origin.remoteAddress
    val city = call.request.headers["cf-ipcity"] ?: "?"
    val country = call.request.headers["cf-ipcountry"] ?: "?"
    val timezone = call.request.headers["cf-timezone"] ?: "?"
    val userAgent = call.request.headers["User-Agent"] ?: ""
    return "${colored(httpMethod.value, Ansi.Color.CYAN)} - ${path()} in ${call.processingTimeMillis(clock)}ms" +
        " ${colored(ip, Ansi.Color.YELLOW)} ${colored("$city, $country ($timezone TZ)", Ansi.Color.BLUE)} ${
          colored(
            userAgent,
            Ansi.Color.WHITE
          )
        }"
  }

  private fun colored(status: HttpStatusCode): String {
    try {
      if (isColorsEnabled && !AnsiConsole.isInstalled()) {
        AnsiConsole.systemInstall()
      }
    } catch (cause: Throwable) {
      isColorsEnabled = false // ignore colors if console was not installed
    }

    return when (status) {
      HttpStatusCode.Found, HttpStatusCode.OK, HttpStatusCode.Accepted, HttpStatusCode.Created -> colored(
        status,
        Ansi.Color.GREEN
      )

      HttpStatusCode.Continue, HttpStatusCode.Processing, HttpStatusCode.PartialContent,
      HttpStatusCode.NotModified, HttpStatusCode.UseProxy, HttpStatusCode.UpgradeRequired,
      HttpStatusCode.NoContent -> colored(
        status,
        Ansi.Color.YELLOW
      )

      else -> colored(status, Ansi.Color.RED)
    }
  }

  private fun colored(value: Any, color: Ansi.Color): String =
    if (isColorsEnabled) {
      Ansi.ansi().fg(color).a(value).reset().toString()
    } else value.toString() // ignore color
}