package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
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
    Text("Loading...")
  } else {
    H1(attrs = { classes(TextStyle.titleText) }) {
      Text(popAlbum.album.name)
    }

    Div(attrs = { classes(AppStyle.divWrapper) }) {
      popAlbum.photos.forEach { photo ->
        PhotoThumbnail(popAlbum.album, photo)
      }
    }
  }
}