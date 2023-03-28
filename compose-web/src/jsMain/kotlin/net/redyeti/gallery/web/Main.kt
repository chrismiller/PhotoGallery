package net.redyeti.gallery.web

import androidx.compose.runtime.*
import kotlinx.coroutines.InternalCoroutinesApi
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStylesheet
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val koin = initKoin(enableNetworkLogs = true).koin

@InternalCoroutinesApi
fun main() {
  val repo = koin.get<PhotoGalleryInterface>()

  renderComposable(rootElementId = "root") {
    Style(AppStylesheet)

    var albums by remember { mutableStateOf(emptyList<Album>()) }

    LaunchedEffect(true) {
      albums = repo.fetchAlbums()
    }

    val gpsCoordinates by produceState(initialValue = GpsCoordinates(0.0, 0.0, 0.0), repo) {
      repo.pollISSPosition().collect { value = it }
    }

    Div(attrs = { style { padding(16.px) } }) {
      H1(attrs = { classes(AppStylesheet.titleText) }) {
        Text("Photo Gallery")
      }
      H2 {
        Text("Location: latitude = ${gpsCoordinates.latitude}, longitude = ${gpsCoordinates.longitude}")
      }

      H1 {
        Text("Albums")
      }
      albums.forEach { album ->
        A(href = "album/${album.id}") {
          Div(
            attrs = {
              style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
              }
            }
          ) {
            Img(
              src = "/image/${album.directory}/thumb/${album.coverImage}",
              attrs = {
                style {
                  width(48.px)
                  property("padding-right", 16.px)
                }
              }
            )

            Span(attrs = { classes(AppStylesheet.albumText) }) {
              Text("${album.name} (${album.year})")
            }
          }
        }
      }
    }
  }
}