package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div
import kotlin.math.max
import kotlin.math.min

@Routing
@Composable
fun RouteBuilder.MapLibrePage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var photoID by remember { mutableStateOf(-1) }

  string { albumKey ->
    int {
      // A photo ID was provided
      photoID = max(0, it)
    }
    noMatch {
      photoID = -1
    }
    LaunchedEffect(albumKey) {
      album = repo.fetchAlbum(albumKey)
      photoID = min(photoID, album!!.photos.size - 1)
      if (photoID >= 0) {
        Preloader.imgPreload(album!!.imageUrl(photoID))
      }
    }
  }

  noMatch {
    album = null
  }

  val popAlbum = album
  if (popAlbum == null) {
    Div(attrs = { classes(AppStyle.loader) })
  } else {
    Page(
      popAlbum.album.title,
      attrs = {
        style {
          height(100.vh)
        }
      },
      header = { AppHeader(popAlbum.album.title, popAlbum.album.subtitle) }
    ) {
      PhotoMapLibre(popAlbum)
    }

    if (photoID >= 0) {
      PhotoPopup(popAlbum, photoID, "/map")
    }
  }
}