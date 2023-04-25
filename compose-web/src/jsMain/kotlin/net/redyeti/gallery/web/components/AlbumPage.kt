package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import kotlin.math.max
import kotlin.math.min

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var albumWidth by remember { mutableStateOf(window.innerWidth - 20) }

  var photoID = 0
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

    DisposableEffect(albumID) {
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
    Text("Loading...")
  } else {
    H1(attrs = { classes(TextStyle.titleText) }) {
      Text(popAlbum.album.name)
    }
    H3 {
      Text("${popAlbum.album.monthName} ${popAlbum.album.year}")
    }
    AlbumGrid(popAlbum, albumWidth)

    if (photoID >= 0) {
      Lightbox {
        val photo = popAlbum.photos[photoID]
        LightboxImage(
          "/image/${popAlbum.album.directory}/large/${photo.filename}",
          Count(photoID, popAlbum.photos.size)
        ) {
          Text(photo.description)
          Small {
            Text("by Chris Miller")
          }
        }
      }
    }
  }
}