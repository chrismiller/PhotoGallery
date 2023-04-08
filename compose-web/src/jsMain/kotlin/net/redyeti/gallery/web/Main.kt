package net.redyeti.gallery.web

import androidx.compose.runtime.*
import kotlinx.coroutines.InternalCoroutinesApi
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.components.AlbumCover
import net.redyeti.gallery.web.style.AppStyleSheet
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

private val koin = initKoin(enableNetworkLogs = true).koin

@InternalCoroutinesApi
fun main() {
  val repo = koin.get<PhotoGalleryInterface>()

  renderComposable(rootElementId = "root") {
    Style(AppStyleSheet)

    var albums by remember { mutableStateOf(emptyList<Album>()) }

    LaunchedEffect(true) {
      albums = repo.fetchAlbums()
    }

    val gpsCoordinates by produceState(initialValue = GpsCoordinates(0.0, 0.0, 0.0), repo) {
      repo.pollISSPosition().collect { value = it }
    }

    H1(attrs = { classes(TextStyle.titleText) }) {
      Text("Photo Gallery")
    }
    H2 {
      Text("Location: latitude = ${gpsCoordinates.latitude}, longitude = ${gpsCoordinates.longitude}")
    }

    H1 {
      Text("Albums")
    }

    Div(attrs = { classes(AppStyleSheet.divWrapper) }) {
      albums.forEach { album ->
        AlbumCover(album)
      }
    }
  }
}