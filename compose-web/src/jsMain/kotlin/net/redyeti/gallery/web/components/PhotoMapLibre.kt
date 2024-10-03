package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import js.objects.jso
import kotlinx.browser.document
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.maplibre.LibreMap
import net.redyeti.maplibre.MapOptions
import net.redyeti.maplibre.jsobject.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.math.max
import kotlin.math.min

private fun getBounds(photos: List<Photo>, padding: Double = 0.10): LngLatBounds {
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
  val padLong = (maxLong - minLong) * padding
  val padLat = (maxLat - minLat) * padding
  minLong = minLong - padLong
  maxLong = maxLong + padLong
  minLat = minLat - padLat
  maxLat = maxLat + padLat
  return LngLatBounds(LngLat(minLong, minLat), LngLat(maxLong, maxLat))
}

const val markerImageSize = 50

@Composable
fun PhotoMapLibre(album: PopulatedAlbum) {
  Div(
    attrs = {
      id("map-container")
      style {
        width(100.percent)
        height(100.percent)
        backgroundColor(Color.darkblue)
      }
    }
  )

  val mapOptions: MapOptions by remember {
    mutableStateOf(
      MapOptions(
        container = "map-container",
        bounds = getBounds(album.photos),
        style = "/liberty-style.json",
      )
    )
  }

  LibreMap(mapOptions) {
    album.photos.forEach { photo ->
      val l = photo.location
      if (l != null) {
        val popup = Popup(null).setText("${photo.filename} Lat: ${l.latitude}, Long: ${l.longitude}")

        val el = document.createElement("div") as HTMLElement
        renderComposable(el) {
          val width = min(markerPhotoSize, photo.width * markerPhotoSize / photo.height)
          val height = min(markerPhotoSize, photo.height * markerPhotoSize / photo.width)
          Img(
            attrs = {
              classes(AppStyle.thumb)
              style {
                width(width.px)
                height(height.px)
              }
            },
            src = photo.thumbnailUrl,
            alt = photo.description
          )
        }
        val marker = Marker(jso { element = el })
          .setLngLat(LngLat(l.longitude, l.latitude))
          .setPopup(popup)
          .addTo(this)
      }
    }
  }
}

internal fun newMarker(options: MarkerOptions): Marker {
  return js("new maplibregl.MarkerOptions(options);") as Marker
}
