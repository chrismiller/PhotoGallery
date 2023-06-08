package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.events.Event
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
  // 0.9 = adjustment for pageWrapper.padding left+right
  fun albumWidth() = (window.innerWidth * 0.9).roundToInt()

  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var albumWidth by remember { mutableStateOf(albumWidth()) }
  var photoID = -1

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
        albumWidth = albumWidth()
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
    Div(attrs = { classes(AppStyle.loader) }) {}
  } else {
    Page(popAlbum.album.title, header = { AppHeader(popAlbum.album.title, popAlbum.album.subtitle) }) {
      AlbumGrid(popAlbum, albumWidth)
    }

    if (photoID >= 0) {
      PhotoPopup(popAlbum, photoID, "/album")
    }
  }
}