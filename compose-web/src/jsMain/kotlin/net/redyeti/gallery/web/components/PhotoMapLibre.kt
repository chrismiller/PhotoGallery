package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import js.objects.jso
import kotlinx.browser.document
import kotlinx.dom.addClass
import net.redyeti.gallery.remote.*
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.maplibre.LibreMap
import net.redyeti.maplibre.MapOptions
import net.redyeti.maplibre.jsobject.LngLat
import net.redyeti.maplibre.jsobject.LngLatBounds
import net.redyeti.maplibre.jsobject.Marker
import net.redyeti.maplibre.jsobject.geojson.*
import net.redyeti.maplibre.jsobject.stylespec.*
import net.redyeti.maplibre.updateMarkers
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Image
import kotlin.math.max
import kotlin.math.min


private fun getBounds(photos: List<Photo>, padding: Double = 0.10): LngLatBounds {
  var minLat = 90.0
  var maxLat = -90.0
  var minLong = 180.0
  var maxLong = -180.0
  val locations = photos.mapNotNull { it.location }
  // TODO: handle the case where no photos are geotagged
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
  println("Bounds: $minLat, $minLong, $maxLat, $maxLong")
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
      // Are there situations where this shouldn't be called here?
      createMapDiv(album)
    }
    on("data") { e ->
      // Based on the example https://maplibre.org/maplibre-gl-js/docs/examples/cluster-html/
      if (e.sourceId == "photos" && e.isSourceLoaded) {
        on("move") { updateMarkers("photos", ::createMarker) }
        on("moveend") { updateMarkers("photos", ::createMarker) }
        updateMarkers("photos", ::createMarker)
      }
    }

//    album.photos.forEach { photo ->
//      val l = photo.location
//      if (l != null) {
//        val markerThumb = document.createElement("div") as HTMLElement
//        renderComposable(markerThumb) {
//          val width = min(markerPhotoSize, photo.width * markerPhotoSize / photo.height)
//          val height = min(markerPhotoSize, photo.height * markerPhotoSize / photo.width)
//          Img(
//            attrs = {
//              classes(AppStyle.thumb)
//              style {
//                width(width.px)
//                height(height.px)
//              }
//            },
//            src = photo.thumbnailUrl,
//            alt = photo.description
//          )
//        }
//        val router = Router.current
//        markerThumb.addEventListener("click", { event ->
//          router.navigate("/map/${album.album.key}/${photo.id}")
//        })
//        Marker(jso { element = markerThumb })
//          .setLngLat(LngLat(l.longitude, l.latitude))
//          .addTo(this)
//      }
//    }
  }
}

fun net.redyeti.maplibre.jsobject.Map.createMapDiv(album: PopulatedAlbum) {
  val source = createSource(album)
  val layer1 = createClusterLayer()
  val layer2 = createClusterCountLayer()
  val layer3 = createUnclusteredLayer()
  //  div.appendText(JSON.stringify(source))
  //  div.appendText("*******")
  //  div.appendText(JSON.stringify(layer3))
  //  div.appendText("*******")
  //  div.appendText(JSON.stringify(layer2))
  //  div.appendText("*******")
  //  div.appendText(JSON.stringify(layer1))
  //  div.appendText("*******")

  addSource("photos", source)
  addLayer(layer1)
  addLayer(layer2)
  addLayer(layer3)
}

fun createMarker(feature: MapGeoJSONFeature): Marker {
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
  img.addEventListener("click", { println("Clicked: /map/$albumKey/$photoId") })
  markerThumb.appendChild(img)
//  val router = Router.current
//  markerThumb.addEventListener("click", { event ->
//    router.navigate("/map/$albumKey/${photo.id}")
//  })
  return Marker(jso { element = markerThumb })
    .setLngLat(LngLat(coords[0], coords[1]))
}

private fun createSource(album: PopulatedAlbum): SourceSpecification {
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
  val source = GeoJSONSourceSpecification(
    type = SourceType.GeoJSON,
    cluster = true,
    clusterMaxZoom = 12.0,  // Disable clustering once we zoom in beyond this
    clusterRadius = 30.0,   // How big an area to cluster
    data = geoData,
  )
  return source
}

private fun createClusterLayer(): SourceLayerSpecification {
  // This works
  return jso<CircleLayerSpecification> {
    id = "clusters"
    type = LayerType.Circle
    source = "photos"
    filter = arrayOf("has", "point_count")
    paint = jso {
      circleColor = arrayOf(
        "interpolate-hcl", arrayOf("linear"),
        arrayOf("get", "point_count"),
        2, "#00F",
        20, "#08F",
        50, "#0FF",
        150, "#0F8",
        500, "#0F0"
      )
      circleRadius = arrayOf("step", arrayOf("get", "point_count"), 20, 100, 30, 750, 40)
    }
  }
}

private fun createClusterCountLayer(): SourceLayerSpecification {
  return jso<SymbolLayerSpecification> {
    id = "cluster-count"
    type = LayerType.Symbol
    source = "photos"
    filter = arrayOf("has", "point_count")
    layout = jso {
      textField = "{point_count_abbreviated}"
      // Note that if the font name doesn't exist (in the style json), the layers silently fail to render. Hard to debug!
      textFont = arrayOf("Noto Sans Regular")
      textSize = 12.0
    }
  }
}

private fun createUnclusteredLayer(): SourceLayerSpecification {
  // Use this as a placeholder, so we know where to add markers for photo thumbnails
  return CircleLayerSpecification(
    id = "unclustered-photos",
    type = LayerType.Circle,
    source = "photos",
    filter = arrayOf("!=", "cluster", true)
  )
}