package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Routing
@Composable
fun RouteBuilder.MapPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }

  int { albumID ->
    LaunchedEffect(albumID) {
      album = repo.fetchAlbum(albumID)
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
      "${popAlbum.album.title} - Location Map",
      popAlbum.album.subtitle,
      attrs = {
        style {
          height(100.vh)
        }
      }
    ) {
      PhotoMap(popAlbum)
    }
  }
}