package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import kotlinx.browser.window
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.maplibre.LibreMap
import net.redyeti.maplibre.jsobject.LngLatBounds
import net.redyeti.maplibre.jsobject.MapOptions
import org.jetbrains.compose.web.dom.Div
import kotlin.math.max
import kotlin.math.min

private class MlState(album: PopulatedAlbum, mapWidth: Double, mapHeight: Double) {
  val photos = album.photos
  var selectedPhoto: Photo? by mutableStateOf(null)
}

private fun getBounds(photos: List<Photo>): LngLatBounds {
  var minLat = 90.0
  var maxLat = -90.0
  var minLong = 180.0
  var maxLong = -180.0
  val locations = photos.mapNotNull { it.location }
  locations.forEach { coord ->
    minLat = min(minLat, coord.latitude)
    maxLat = max(maxLat, coord.latitude)
    minLong = min(minLong, coord.longitude)
    maxLong = max(maxLong, coord.longitude)
  }
  return LngLatBounds(minLong, maxLat, maxLong, minLat)
}

@Composable
fun PhotoMapLibre(album: PopulatedAlbum) {
  val mapWidth = window.innerWidth * 0.8
  val mapHeight = window.innerHeight * 0.7
  val state = MlState(album, mapWidth, mapHeight)

  Div(attrs = { id("map-container") })

  val mapOptions by remember {
    mutableStateOf(
      MapOptions().apply {
        bounds = getBounds(state.photos)
        container = "map-container"
        style = "https://demotiles.maplibre.org/style.json"
        zoom = 9.0
      }
    )
  }

  LibreMap("mymap", mapOptions)

  val map = net.redyeti.maplibre.jsobject.Map(mapOptions).apply { }
}
