package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun RouteBuilder.PhotoPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var photoId by remember { mutableStateOf(-1) }

  int { albumID ->
    int { photoID ->
      if (albumID >= 0 && photoID >= 0) {
        LaunchedEffect(albumID * 10000 + photoID) {
          val popAlbum = repo.fetchAlbum(albumID)
          if (popAlbum.photos.size > photoID) {
            album = popAlbum
            photoId = photoID
          }
        }
      }
    }
  }

  val popAlbum = album
  if (popAlbum == null || photoId < 0) {
    Text("Loading album...")
  } else {
    val photo = popAlbum.photos[photoId]
    Img(
      src = "/image/${popAlbum.album.directory}/large/${photo.filename}",
      alt = photo.description
    )
  }
}