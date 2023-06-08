package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Router
import kotlinx.browser.document
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text

@Composable
fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, base: String) {
  var id by remember { mutableStateOf(photoID) }

  val router = Router.current

  val prevID = popAlbum.wrappedID(id - 1)
  val prevUrl = "$base/${popAlbum.album.id}/$prevID"
  val nextID = popAlbum.wrappedID(id + 1)
  val nextUrl = "$base/${popAlbum.album.id}/$nextID"
  val close = { router.navigate("$base/${popAlbum.album.id}") }

  DisposableEffect(id) {
    document.onkeydown = { e ->
      when (e.key) {
        "Escape" -> {
          close()
        }

        "ArrowLeft" -> {
          router.navigate(prevUrl)
          id = prevID
        }

        "ArrowRight" -> {
          router.navigate(nextUrl)
          id = nextID
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
      NavLink(prevUrl) {
        Button(attrs = {
          title("Previous (Left arrow key)")
          classes(LightboxStyle.arrow, LightboxStyle.arrowLeft)
          onClick { id = prevID }
        }
        ) {}
      }
    },
    next = {
      NavLink(nextUrl) {
        Button(attrs = {
          title("Next (Right arrow key)")
          classes(LightboxStyle.arrow, LightboxStyle.arrowRight)
          onClick { id = nextID }
        }
        ) {}
      }
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
