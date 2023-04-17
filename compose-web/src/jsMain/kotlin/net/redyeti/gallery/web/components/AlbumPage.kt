package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var albumWidth by remember { mutableStateOf(window.innerWidth - 20) }

  int { albumID ->
    LaunchedEffect(albumID) {
      album = repo.fetchAlbum(albumID)
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

  val popAlbum = album
  if (popAlbum == null) {
    Text("Loading...")
  } else {
    H1(attrs = { classes(TextStyle.titleText) }) {
      Text(popAlbum.album.name)
    }
    H3 {
      val monthName = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
      )[popAlbum.album.month - 1]
      Text("$monthName ${popAlbum.album.year}")
    }
    AlbumGrid(popAlbum, albumWidth)
  }
}