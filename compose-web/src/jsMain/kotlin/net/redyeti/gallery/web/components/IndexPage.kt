package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.*
import kotlin.js.Date

@Routing
@Composable
fun IndexPage(repo: PhotoGalleryInterface) {
  var albums by remember { mutableStateOf(emptyList<Album>()) }

  LaunchedEffect(true) {
    albums = repo.fetchAlbums()
  }

  Div(attrs = { classes(AppStyle.pageWrapper)}) {
    Header {
      AppHeader("Travel Photos", "Chris Miller")
    }
    Main {
      Div(attrs = { classes(AppStyle.wrapper)}) {
        albums.forEach { album ->
          AlbumCover(album)
        }
      }
    }
    Footer {
      val year = Date().getFullYear()
      Text("Copyright © Chris Miller, 2005-$year. All rights reserved.")
    }
  }
}