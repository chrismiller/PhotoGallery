package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import js.objects.jso
import kotlinx.browser.document
import kotlinx.dom.addClass
import net.redyeti.gallery.remote.*
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.maplibre.LibreMap
import net.redyeti.maplibre.MapOptions
import net.redyeti.maplibre.resetCache
import net.redyeti.maplibre.jsobject.LngLat
import net.redyeti.maplibre.jsobject.LngLatBounds
import net.redyeti.maplibre.jsobject.Marker
import net.redyeti.maplibre.jsobject.geojson.*
import net.redyeti.maplibre.jsobject.stylespec.*
import net.redyeti.maplibre.jsobject.stylespec.CirclePaintConfig
import net.redyeti.maplibre.updateMarkers
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Image
import kotlin.math.max
import kotlin.math.min

private const val markerPhotoSize = 100

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
  minLong = max(minLong - padLong, -180.0)
  maxLong = min(maxLong + padLong, 180.0)
  minLat = max(minLat - padLat, -90.0)
  maxLat = min(maxLat + padLat, 90.0)

  return LngLatBounds(LngLat(minLong, minLat), LngLat(maxLong, maxLat))
}

@Composable
fun PhotoMapLibre(album: PopulatedAlbum) {
  Div(
    attrs = {
      id("map-container")
      style {
        width(100.percent)
        height(100.percent)
        backgroundColor(Color.black)
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
    on("load") {
      // Clear the cache, because photo IDs are different compared to the previous album's data,
      // plus we're not likely to want the entries again soon.
      resetCache()
      // Are there situations where this shouldn't be called here?
      populateMap(album)
    }
    val router = Router.current
    val createMarker = { feature: MapGeoJSONFeature ->
      createMarker(feature) { albumKey, photoId ->
        router.navigate("/map/${albumKey}/${photoId}")
      }
    }
    on("data") { e ->
      // Based on the example https://maplibre.org/maplibre-gl-js/docs/examples/cluster-html/
      if (e.sourceId == "photos" && e.isSourceLoaded) {
        on("move") { updateMarkers("photos", 13.0, 14.0, createMarker) }
        on("moveend") { updateMarkers("photos", 13.0, 14.0, createMarker) }
        updateMarkers("photos", 13.0, 14.0, createMarker)
      }
    }
  }
}

fun net.redyeti.maplibre.jsobject.Map.populateMap(album: PopulatedAlbum) {
  if (album.album.hasGpsTracks) {
    val gpsSource = createGpsTracks(album)
    addSource("gpstracks", gpsSource)
    addLayer(createGpsTrackLayer())
  }

  addSource("photos", createPhotoSource(album))
  addLayer(createHeatmapLayer())
  addLayer(createThumbnailLayer())
}

fun createMarker(feature: MapGeoJSONFeature, onClick: (albumKey: String, photoId: Int) -> Unit): Marker {
  val coords = (feature.geometry.unsafeCast<Point>()).coordinates
  val markerThumb = document.createElement("div") as HTMLDivElement
  val photoId = feature.properties.id.unsafeCast<Int>()
  val thumbnailUrl = feature.properties.thumbnailUrl.unsafeCast<String>()
  val description = feature.properties.description.unsafeCast<String>()
  val width = feature.properties.width.unsafeCast<Int>()
  val height = feature.properties.height.unsafeCast<Int>()
  val albumKey = feature.properties.albumKey.unsafeCast<String>()
  val markerWidth = min(markerPhotoSize, width * markerPhotoSize / height)
  val markerHeight = min(markerPhotoSize, height * markerPhotoSize / width)
  val img = Image()
  img.addClass(AppStyle.thumb)
  img.src = thumbnailUrl
  img.alt = description
  img.style.width = "${markerWidth}px"
  img.style.height = "${markerHeight}px"
  img.addEventListener("click", { onClick(albumKey, photoId) })
  markerThumb.appendChild(img)
  markerThumb.style.cursor = "pointer"
  return Marker(jso { element = markerThumb })
    .setLngLat(LngLat(coords[0], coords[1]))
}

private fun createPhotoSource(album: PopulatedAlbum): SourceSpecification {
  val geoData: FeatureCollection<GeoJsonObject, GeoJsonProperties> = jso {
    type = GeoJsonTypes.FeatureCollection
    features = album.photos.mapNotNull<Photo, Feature<GeoJsonObject, GeoJsonProperties>> {
      it.location?.let { location ->
        jso {
          type = GeoJsonTypes.Feature
          geometry = jso<Point> {
            type = GeoJsonTypes.Point
            coordinates = arrayOf(location.longitude, location.latitude, location.altitude)
            properties = jso {
              set("id", it.id)
              set("thumbnailUrl", it.thumbnailUrl)
              set("description", it.description)
              set("width", it.width)
              set("height", it.height)
              set("albumKey", album.album.key)
            }
          }
        }
      }
    }.toTypedArray()
  }
  return GeoJSONSourceSpecification(
    type = SourceType.GeoJSON,
    data = geoData,
  )
}

private fun createGpsTracks(album: PopulatedAlbum): SourceSpecification {
  return GeoJSONSourceSpecification(
    type = SourceType.GeoJSON,
    data = album.album.gpsUrl,
  )
}

private fun createHeatmapLayer(): SourceLayerSpecification {
  return jso<HeatmapLayerSpecification> {
    id = "heatmap"
    type = LayerType.HeatMap
    source = "photos"
    maxzoom = 14.0
    paint = jso {
      // We don't really have anything to weight this by? :-/
      heatmapWeight = 1.0
      // Increase the heatmap color weight by zoom level. heatmap-intensity is a multiplier on top of heatmap-weight
      heatmapIntensity = arrayOf("interpolate", arrayOf("linear"), arrayOf("zoom"), 0, 1, 14, 3)
      // Colour ramp for heatmap.  Domain is 0 (low) to 1 (high). Begin colour ramp at 0-stop with a
      // 0-transparency colour to create a blur-like effect.
      heatmapColor = arrayOf("interpolate", arrayOf("linear"), arrayOf("heatmap-density"),
        0,
        "rgba(50, 50, 255, 0)",
        0.2,
        "rgba(0, 204, 255, 0.2)",
        0.4,
        "rgba(128, 255, 128, 0.6)",
        0.6,
        "rgb(255, 255, 102)",
        0.8,
        "rgb(255, 128, 0)",
        1,
        "rgb(204, 0, 0)"
      )
      // Adjust the heatmap radius by zoom level
      heatmapRadius = arrayOf("interpolate", arrayOf("linear"), arrayOf("zoom"), 0, 2, 14, 20)
      // Fade the heatmap out once we start zooming in past level 10
      heatmapOpacity = arrayOf("interpolate", arrayOf("linear"), arrayOf("zoom"), 12, 1, 14, 0)
    }
  }
}

private fun createThumbnailLayer(): SourceLayerSpecification {
  // Use this as an invisible placeholder, so we know where to add markers for photo thumbnails.
  return CircleLayerSpecification(
    id = "thumbnails",
    type = LayerType.Circle,
    source = "photos",
    minzoom = 14.0,
    paint = jso<CirclePaintConfig> {
      circleColor = "rgba(0,0,0,0)"
      circleRadius = 1
    }
  )
}

private fun createGpsTrackLayer(): SourceLayerSpecification {
  // Use this as an invisible placeholder, so we know where to add markers for photo thumbnails.
  return LineLayerSpecification(
    id = "gpstracks",
    type = LayerType.Line,
    source = "gpstracks",
    minzoom = 5.0,
    layout = jso<LineLayoutConfig> {
      lineJoin = LineJoinType.Round
      lineCap = LineCapType.Round
    },
    paint = jso<LinePaintConfig> {
      lineColor = "rgba(0,0,180,0.5)"
      lineWidth = 3.0
    }
  )
}
