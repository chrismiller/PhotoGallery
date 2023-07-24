package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

data class Count(val current: Int, val total: Int)

@Composable
fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, base: String) {
  var id by remember { mutableStateOf(photoID) }
  var imageUrl: String? by remember { mutableStateOf(null) }

  val urlToLoad = popAlbum.imageUrl(id)
  Preloader.imgPreload(urlToLoad) { imageUrl = urlToLoad }

  val updateId: (Int) -> Unit = { newId ->
    val newID = popAlbum.wrappedID(newId)
    val newUrl = "$base/${popAlbum.album.id}/$newID"
    window.history.replaceState(null, "", newUrl)
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

  val url = imageUrl
  if (url != null) {
    Div(attrs = {
      id("fs")
      classes(LightboxStyle.lightbox)
    }) {
      NavButton("Previous (left arrow key)", LightboxStyle.arrowPrev, prev)
      NavButton("Next (right arrow key)", LightboxStyle.arrowNext, next)
      PopupImage(url, close)
      val photo = popAlbum.photos[id]
      PhotoCaption(photo, Count(id, popAlbum.photos.size))
    }
  }

  // Preload a few adjacent images, so they can be displayed quickly when needed
  for (i in listOf(-1, 1, 2, 3)) {
    Preloader.imgPreload(popAlbum.imageUrl(id + i))
  }
}

@Composable
fun PopupImage(imageUrl: String, close: () -> Unit) {
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
fun NavButton(text: String, style: String, update: () -> Unit) {
  A(attrs = {
    title(text)
    classes(LightboxStyle.arrow, style)
    onClick {
      update()
    }
  }
  ) {}
}