package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.redyeti.gallery.remote.CameraDetails
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun InfoPanel(photo: Photo, close: () -> Unit) {
  Aside(attrs = { classes(LightboxStyle.infoPanel) }) {
    H2(attrs = { classes(LightboxStyle.infoPanelHeader) }) {
      Text("Info")
      Button(attrs = {
        classes(LightboxStyle.closeInfoPanel)
        onClick { close() }
      }) {}
    }
    Div(attrs = { classes(LightboxStyle.infoSection) }) {
      FileInfoPanel(photo)
      DateInfoPanel(photo.epochSeconds, photo.timeOffset)
      CameraInfoPanel(photo.cameraDetails)
      LocationInfoPanel(photo.location)
    }
  }
}

@Composable
private fun InfoItem(style: String, content: @Composable () -> Unit) {
  Div(attrs = { classes(LightboxStyle.infoItem) }) {
    Div(attrs = { classes(LightboxStyle.detailItem, style) }) {
      content()
    }
  }
}

@Composable
private fun FileInfoPanel(photo: Photo) {
  InfoItem(LightboxStyle.fileInfo) {
    H4 { Text("File info") }
    Span(attrs = { classes(LightboxStyle.label) }) { Text(photo.filename) }
    Span(attrs = { classes(LightboxStyle.subs) }) {
      Span { Text("${photo.originalWidth} × ${photo.originalHeight}") }
      Span { Text((photo.originalWidth * photo.originalHeight).humanReadableMp()) }
      Span { Text(photo.originalSize.humanReadableSize()) }
    }
  }
}

private fun Int.humanReadableMp(): String {
  return "${(this / 1_000_000.0).toStringOneDp()} MP"
}

private fun Int.humanReadableSize(): String {
  val kb = this / 1024.0
  if (kb < 1024) return "${kb.toStringOneDp()} KB"
  return "${(kb / 1024.0).toStringOneDp()} MB"
}

private fun Double.toStringOneDp(): String {
  val rounded = (this * 10.0).roundToInt()
  return "${rounded / 10}.${rounded % 10}"
}

@Composable
private fun DateInfoPanel(epochSeconds: Long, timeOffset: String) {
  InfoItem(LightboxStyle.dateInfo) {
    H4 { Text("Date taken") }
    val instant = Instant.fromEpochSeconds(epochSeconds)
    val timezone = TimeZone.of(timeOffset)
    val ldt = instant.toLocalDateTime(timezone)
    val dateStr = "${ldt.month.name.firstUpper()} ${ldt.dayOfMonth}, ${ldt.year}"
    val amPm = if (ldt.hour < 12) "AM" else "PM"
    val hour = if (ldt.hour < 13) ldt.hour else if (ldt.hour == 24) 0 else ldt.hour - 12
    val tz = if (timeOffset == "UTC" || timeOffset.lowercase() == "z") "UTC" else "UTC${timeOffset}"
    val timeStr = "${ldt.dayOfWeek.name.firstUpper()}, $hour:${ldt.minute.twoChars()} $amPm $tz"
    Span(attrs = { classes(LightboxStyle.label) }) { Text(dateStr) }
    Span(attrs = { classes(LightboxStyle.subs) }) {
      Span { Text(timeStr) }
    }
  }
}

private fun Int.twoChars(): String {
  return if (this < 10) "0$this" else this.toString()
}

private fun String.firstUpper() = this.lowercase().replaceFirstChar { c -> c.uppercaseChar() }

@Composable
private fun CameraInfoPanel(details: CameraDetails) {
  InfoItem(LightboxStyle.cameraInfo) {
    H4 { Text("Camera") }
    Span(attrs = { classes(LightboxStyle.label) }) { Text(details.camera) }
    Span(attrs = { classes(LightboxStyle.label) }) { Text(details.lens) }
    Span(attrs = { classes(LightboxStyle.subs) }) {
      if (details.focalLength.isNotEmpty()) {
        Span { Text(details.focalLength) }
      }
      if (details.aperture.isNotEmpty()) {
        Span { Text("ƒ/${details.aperture}") }
      }
      if (details.shutterSpeed.isNotEmpty())
        Span { Text("${details.shutterSpeed}s") }
      if (details.iso > 0) {
        Span { Text("ISO ${details.iso}") }
      }
    }
  }
}

@Composable
private fun LocationInfoPanel(location: GpsCoordinates?) {
  if (location == null) {
    return
  }
  InfoItem(LightboxStyle.locationInfo) {
    H4 { Text("Location") }
    if (location.hasCoordinates()) {
      Span(attrs = { classes(LightboxStyle.label) }) { Text("${location.latDMS} ${location.longDMS}") }
      Span(attrs = { classes(LightboxStyle.subs) }) {
        if (location.altitude != 0.0) {
          Span { Text("Altitude: ${location.altitude.toInt()} m") }
        }
        Span(attrs = { classes(LightboxStyle.subs) }) {
          Span {
            A(href = location.googleMapsUrl) {
              Text("Google Maps")
            }
          }
        }
      }
    } else {
      Span(attrs = { classes(LightboxStyle.label) }) { Text("Altitude: ${location.altitude.toInt()} m") }
    }
  }
}
