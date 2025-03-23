package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.*

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
      Header(attrs = {
        classes(LightboxStyle.galleryHeader)
        onClick { e ->
          if (e.target == e.currentTarget) {
            close()
          }
        }
      }) {
        Back(close)
        Options(popAlbum, photo, infoClicked = { infoPanelVisible = !infoPanelVisible })
      }
      Div(attrs = {
        id("fs")
        classes(LightboxStyle.lightbox)
      }) {
        NavButton("Previous (left arrow key)", LightboxStyle.arrowPrev, prev)
        NavButton("Next (right arrow key)", LightboxStyle.arrowNext, next)
        PopupImage(url, close)
        PhotoCaption(photo, Count(id, popAlbum.photos.size), updateId)
      }
      InfoPanel(photo) { infoPanelVisible = false }
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
private fun Options(album: PopulatedAlbum, photo: Photo, infoClicked: () -> Unit) {
  Ul(attrs = { classes(LightboxStyle.options) }) {
    OptionIcon("Show album", LightboxStyle.albumOption) {
      window.location.href = "/album/${album.album.key}"
    }
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
private fun PopupImage(imageUrl: String, close: () -> Unit) {
  Div(attrs = {
    classes(LightboxStyle.imageWrapper)
    onClick { e ->
      // Close the lightbox when clicking on it
      close()
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