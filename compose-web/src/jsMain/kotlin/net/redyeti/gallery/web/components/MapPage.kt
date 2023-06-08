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
fun RouteBuilder.MapPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }

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
      popAlbum.album.subtitle,
      attrs = {
        style {
          height(100.vh)
        }
      }
    ) {
      PhotoMap(popAlbum)
    }

    if (photoID >= 0) {
      PhotoPopup(popAlbum, photoID, "/map")
    }
  }
}