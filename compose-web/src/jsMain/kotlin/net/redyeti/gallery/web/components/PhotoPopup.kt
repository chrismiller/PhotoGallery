package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text

@Composable
fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, base: String) {
  var id by remember { mutableStateOf(photoID) }

  val updateId: (Int) -> Unit = { newId ->
    val newID = popAlbum.wrappedID(newId)
    val newUrl = "$base/${popAlbum.album.id}/$newID"
    window.history.replaceState(null, "", newUrl)
    id = newID
  }

  val router = Router.current
  val close = { router.navigate("$base/${popAlbum.album.id}") }

  DisposableEffect(id) {
    document.onkeydown = { e ->
      when (e.key) {
        "Escape" -> {
          close()
        }

        "ArrowLeft" -> {
          updateId(id - 1)
        }

        "ArrowRight" -> {
          updateId(id + 1)
        }
      }
    }
    onDispose {
      document.onkeydown = null
    }
  }

  Lightbox(
    close = close,
    previous = {
      Button(attrs = {
        title("Previous (Left arrow key)")
        classes(LightboxStyle.arrow, LightboxStyle.arrowLeft)
        onClick {
          updateId(id - 1)
        }
      }
      ) {}
    },
    next = {
      Button(attrs = {
        title("Next (Right arrow key)")
        classes(LightboxStyle.arrow, LightboxStyle.arrowRight)
        onClick {
          updateId(id + 1)
        }
      }
      ) {}
    }
  ) {

    // Preload a few adjacent images, so they can be displayed quickly when needed
    for (i in listOf(-1, 1, 2, 3)) {
      Preloader.imgPreload(popAlbum.imageUrl(id + i))
    }

    val photo = popAlbum.photos[id]
    LightboxImage(
      photo,
      popAlbum.imageUrl(id)
    ) {
      GalleryCaption(photo.location, Count(id, popAlbum.photos.size)) {
        Text(photo.description)
        Small {
          Text("Copyright © Chris Miller")
        }
      }
    }
  }
}
