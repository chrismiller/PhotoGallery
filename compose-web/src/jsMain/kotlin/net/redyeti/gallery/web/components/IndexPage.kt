package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun RouteBuilder.IndexPage(repo: PhotoGalleryInterface) {
  var albums by remember { mutableStateOf(emptyList<Album>()) }

  LaunchedEffect(true) {
    albums = repo.fetchAlbums()
  }

//  val gpsCoordinates by produceState(initialValue = GpsCoordinates(0.0, 0.0, 0.0), repo) {
//    repo.pollISSPosition().collect { value = it }
//  }

  H1(attrs = { classes(TextStyle.titleText) }) {
    Text("Photo Gallery")
  }
  H2 {
//    Text("Location: latitude = ${gpsCoordinates.latitude}, longitude = ${gpsCoordinates.longitude}")
  }

  H1 {
    Text("Albums")
  }

  Div(attrs = { classes(AppStyle.divWrapper) }) {
    albums.forEach { album ->
      AlbumCover(album)
    }
  }
}