package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Router
import app.softwork.routingcompose.Routing
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.LightboxStyle
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.events.Event
import kotlin.math.max
import kotlin.math.min

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var albumWidth by remember { mutableStateOf(window.innerWidth - 20) }

  var photoID = 0
  val router = Router.current

  int { albumID ->
    int {
      // A photo ID was provided
      photoID = max(0, it)
    }
    noMatch {
      photoID = -1
    }
    LaunchedEffect(albumID) {
      album = repo.fetchAlbum(albumID)
      photoID = min(photoID, album!!.photos.size - 1)
    }

    DisposableEffect(albumWidth) {
      val resizeListener: (Event) -> Unit = {
        albumWidth = window.innerWidth - 20
      }
      window.addEventListener("resize", resizeListener)
      onDispose {
        window.removeEventListener("resize", resizeListener)
      }
    }
  }

  noMatch {
    album = null
  }

  NavLink(to = "/") {
    Text("<- Back to album index")
  }

  val popAlbum = album
  if (popAlbum == null) {
    Div(attrs = { classes(AppStyle.loader) }) {}
  } else {
    H1(attrs = { classes(TextStyle.titleText) }) {
      Text(popAlbum.album.title)
    }
    H3 {
      Text(popAlbum.album.subtitle)
    }
    AlbumGrid(popAlbum, albumWidth)

    if (photoID >= 0) {
      PhotoPopup(popAlbum, photoID) {
        router.navigate("/album/${popAlbum.album.id}")
      }
    }
  }
}

@Composable
private fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, close: () -> Unit) {
  var id by remember { mutableStateOf(photoID) }

  val router = Router.current

  val prevID = popAlbum.wrappedID(id - 1)
  val prevUrl = "/album/${popAlbum.album.id}/$prevID"
  val nextID = popAlbum.wrappedID(id + 1)
  val nextUrl = "/album/${popAlbum.album.id}/$nextID"

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
    val photo = popAlbum.photos[id]
    val imageUrl = "/image/${popAlbum.album.directory}/large/${photo.filename}"
    LightboxImage(
      imageUrl,
      Count(id, popAlbum.photos.size),
      close
    ) {
      Text(photo.description)
      Small {
        Text("by Chris Miller")
      }
    }
  }
}

fun PopulatedAlbum.wrappedID(id: Int) = (id + photos.size) % photos.size
