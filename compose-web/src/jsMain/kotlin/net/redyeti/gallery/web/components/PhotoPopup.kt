package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.sizedSVG
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, base: String) {
  var id by remember { mutableStateOf(photoID) }
  var loaded by remember { mutableStateOf(false) }

  val updateId: (Int) -> Unit = { newId ->
    val newID = popAlbum.wrappedID(newId)
    val newUrl = "$base/${popAlbum.album.id}/$newID"
    window.history.replaceState(null, "", newUrl)
    loaded = false
    id = newID
  }

  val router = Router.current
  val close = { router.navigate("$base/${popAlbum.album.id}") }
  val next = { updateId(id + 1) }
  val prev = { updateId(id - 1) }

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
      }
    }
    onDispose {
      document.onkeydown = null
    }
  }

  // Shade out the background behind the lightbox
  Div(attrs = { classes(LightboxStyle.background) })

  val photo = popAlbum.photos[id]
  val imageUrl = if (loaded) popAlbum.imageUrl(id) else sizedSVG(photo.width, photo.height)

  Preloader.imgPreload(imageUrl) { loaded = true }

  Div(attrs = {
    id("fs")
    classes(LightboxStyle.lightbox)
  }) {
    PopupImage(photo, imageUrl, loaded, close)
    PhotoCaption(photo, Count(id, popAlbum.photos.size))
    NavButton("Previous (left arrow key)", LightboxStyle.arrowLeft, prev)
    NavButton("Next (right arrow key)", LightboxStyle.arrowRight, next)
  }

  // Preload a few adjacent images, so they can be displayed quickly when needed
  for (i in listOf(-1, 1, 2, 3)) {
    Preloader.imgPreload(popAlbum.imageUrl(id + i))
  }
}

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun PopupImage(photo: Photo, imageUrl: String, loaded: Boolean, close: () -> Unit) {
  if (loaded) {
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
  } else {
    Div {
      Img(src = sizedSVG(photo.width, photo.height), attrs = { classes(LightboxStyle.image) })
      Span(attrs = {
        style {
          position(Position.Absolute)
          left(50.percent)
          top(50.percent)
          transform { translate((-50).percent, (-50).percent) }
        }
      }) {
        Img(src = "/loading.svg")
      }
    }
  }
}

@Composable
fun NavButton(text: String, style: String, update: () -> Unit) {
  Button(attrs = {
    title(text)
    classes(LightboxStyle.arrow, style)
    onClick {
      update()
    }
  }
  ) {}
}