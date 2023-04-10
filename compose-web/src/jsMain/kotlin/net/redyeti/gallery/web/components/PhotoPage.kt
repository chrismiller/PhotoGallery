package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.repository.PhotoGalleryInterface

@Routing
@Composable
fun RouteBuilder.PhotoPage(repo: PhotoGalleryInterface) {
  int { albumID ->
    int { photoID ->
      LaunchedEffect(albumID) {
        val album = repo.fetchAlbum(albumID)
      }
    }
  }
}