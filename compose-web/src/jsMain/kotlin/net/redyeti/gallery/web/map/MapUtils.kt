package net.redyeti.gallery.web.map

import com.chihsuanwu.maps.compose.web.LatLng
import com.chihsuanwu.maps.compose.web.LatLngBounds
import net.redyeti.gallery.remote.GpsCoordinates
import kotlin.math.*

private const val WORLD_DIM = 256.0
private const val ZOOM_MAX = 21.0
private const val LN2 = 0.6931471805599453

data class MapRegion(val centre: LatLng, val zoomLevel: Double)

fun getMapRegion(locations: List<GpsCoordinates>, mapWidth: Double, mapHeight: Double): MapRegion {
  var minLat = 90.0
  var maxLat = -90.0
  var minLong = 180.0
  var maxLong = -180.0
  locations.forEach { coord ->
    minLat = min(minLat, coord.latitude)
    maxLat = max(maxLat, coord.latitude)
    minLong = min(minLong, coord.longitude)
    maxLong = max(maxLong, coord.longitude)
  }
  var midLong = (minLong + maxLong) / 2.0
  if (minLong < -160.0 && maxLong > 160.0) {
    // We're probably straddling the +/-180 line, invert the range
    val tmp = minLong
    minLong = maxLong
    maxLong = tmp
    midLong += if (midLong < 0) 180 else -180
  }
  val centre = LatLng((minLat + maxLat) / 2.0, midLong)
  val zoomLevel = getBoundsZoomLevel(LatLngBounds(maxLong, maxLat, minLat, minLong), mapWidth, mapHeight)
  return MapRegion(centre, zoomLevel)
}

fun getBoundsZoomLevel(bounds: LatLngBounds, mapWidth: Double, mapHeight: Double): Double {
  fun latRad(lat: Double): Double {
    val sinLat = sin(lat * PI / 180.0)
    val radX2 = ln((1 + sinLat) / (1 - sinLat)) / 2.0
    return max(min(radX2, PI), -PI) / 2.0
  }

  fun zoom(mapPx: Double, worldPx: Double, fraction: Double): Double {
    return ln(mapPx / worldPx / fraction) / LN2
  }

  val latFraction = (latRad(bounds.north) - latRad(bounds.south)) / PI

  val lngDiff = bounds.east - bounds.west
  val lngFraction = (if (lngDiff < 0.0) (lngDiff + 360.0) else lngDiff) / 360.0

  val latZoom = zoom(mapHeight, WORLD_DIM, latFraction)
  val lngZoom = zoom(mapWidth, WORLD_DIM, lngFraction)

  return min(min(latZoom, lngZoom), ZOOM_MAX)
}