package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.redyeti.gallery.remote.CameraDetails
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

data class Count(val current: Int, val total: Int)

@Composable
fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, base: String) {
  var id by remember { mutableStateOf(photoID) }
  var imageUrl: String? by remember { mutableStateOf(null) }
  var controlsVisible by remember { mutableStateOf(false) }
  var infoPanelVisible by remember { mutableStateOf(false) }

  val urlToLoad = popAlbum.imageUrl(id)
  Preloader.imgPreload(urlToLoad) { imageUrl = urlToLoad }

  val updateId: (Int) -> Unit = { newId ->
    val newID = popAlbum.wrappedID(newId)
    val newUrl = "$base/${popAlbum.album.key}/$newID"
    window.history.replaceState(null, "", newUrl)
    id = newID
  }

  val router = Router.current
  val close = { router.navigate("$base/${popAlbum.album.key}") }
  val next = { updateId(id + 1) }
  val prev = { updateId(id - 1) }

  DisposableEffect(true) {
    var timerHandle = -1

    fun resetTimer() {
      window.clearTimeout(timerHandle)
      timerHandle = window.setTimeout({
        controlsVisible = false
      }, 2500)
    }

    fun setControlsVisible() {
      resetTimer()
      controlsVisible = true
    }

    document.onpointermove = { setControlsVisible() }
    document.onclick = { setControlsVisible() }
    document.onfocus = { setControlsVisible() }
    onDispose {
      window.clearTimeout(timerHandle)
      document.onpointermove = null
      document.onclick = null
      document.onfocus = null
    }
  }

  DisposableEffect(id) {
    document.onkeydown = { e ->
      when (e.key) {
        "Escape" -> {
          close()
        }

        "ArrowLeft" -> {
          prev()
        }

        "ArrowRight" -> {
          next()
        }

        "i" -> {
          infoPanelVisible = !infoPanelVisible
          e.stopPropagation()
        }
      }
    }
    onDispose {
      document.onkeydown = null
    }
  }

  // Shade out the background behind the lightbox
  Div(attrs = { classes(LightboxStyle.background) })

  val url = imageUrl
  if (url != null) {
    val styles = mutableListOf(LightboxStyle.gallery)
    if (controlsVisible) {
      styles += LightboxStyle.showControls
    }
    if (infoPanelVisible) {
      styles += LightboxStyle.showInfoPanel
    }

    Section(attrs = { classes(styles) }) {
      val photo = popAlbum.photos[id]
      Header(attrs = { classes(LightboxStyle.galleryHeader) }) {
        Back(close)
        Options(photo, infoClicked = { infoPanelVisible = !infoPanelVisible })
      }
      Div(attrs = {
        id("fs")
        classes(LightboxStyle.lightbox)
      }) {
        NavButton("Previous (left arrow key)", LightboxStyle.arrowPrev, prev)
        NavButton("Next (right arrow key)", LightboxStyle.arrowNext, next)
        PopupImage(url, close)
        PhotoCaption(photo, Count(id, popAlbum.photos.size))
      }
      Aside(attrs = { classes(LightboxStyle.infoPanel) }) {
        H2(attrs = { classes(LightboxStyle.infoPanelHeader) }) {
          Text("Info")
          Button(attrs = {
            classes(LightboxStyle.closeInfoPanel)
            onClick { infoPanelVisible = false }
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
  }

  // Preload a few adjacent images, so they can be displayed quickly when needed
  for (i in listOf(-1, 1, 2, 3)) {
    Preloader.imgPreload(popAlbum.imageUrl(id + i))
  }
}

@Composable
private fun Back(close: () -> Unit) {
  A(attrs = {
    classes(LightboxStyle.back)
    onClick { close() }
  }) { Span { Text("Back") } }
}

@Composable
private fun Options(photo: Photo, infoClicked: () -> Unit) {
  Ul(attrs = { classes(LightboxStyle.options) }) {
    val location = photo.location
    if (location != null) {
      OptionIcon("Show location on Google Maps", LightboxStyle.locationOption) {
        window.location.href = location.googleMapsUrl
      }
    }
    OptionIcon("Show photo information", LightboxStyle.infoOption) { infoClicked() }
  }
}

@Composable
fun OptionIcon(text: String, style: String, action: () -> Unit) {
  Li {
    Button(attrs = {
      classes(style)
      title(text)
      onClick { action() }
    }) {}
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

@Composable
private fun PopupImage(imageUrl: String, close: () -> Unit) {
  Div(attrs = {
    classes(LightboxStyle.imageWrapper)
    onClick { e ->
      // Close the lightbox when clicking on it, but only if an arrow or any other higher level item wasn't the target
      if (e.target == e.currentTarget) {
        close()
      }
    }
  }) {
    Img(
      attrs = {
        classes(LightboxStyle.lightboxImage)
      },
      src = imageUrl
    )
  }
}

@Composable
private fun NavButton(text: String, style: String, update: () -> Unit) {
  A(attrs = {
    title(text)
    classes(LightboxStyle.arrow, style)
    onClick { update() }
  }
  ) {}
}