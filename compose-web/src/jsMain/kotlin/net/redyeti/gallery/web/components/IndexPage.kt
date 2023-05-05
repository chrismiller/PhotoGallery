package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Div

@Routing
@Composable
fun IndexPage(repo: PhotoGalleryInterface) {
  var albums by remember { mutableStateOf(emptyList<Album>()) }

  LaunchedEffect(true) {
    albums = repo.fetchAlbums()
  }

  Page("Travel Photos", "Chris Miller") {
    Div(attrs = { classes(AppStyle.coverWrapper) }) {
      albums.forEach { album ->
        AlbumCover(album)
      }
    }
  }
}