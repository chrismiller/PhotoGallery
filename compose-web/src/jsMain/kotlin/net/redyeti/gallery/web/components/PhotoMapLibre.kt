package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import js.objects.jso
import kotlinx.browser.document
import kotlinx.dom.appendText
import kotlinx.js.JsPlainObject
import net.redyeti.gallery.remote.*
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
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
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
      if (document.getElementById("XXXXX") == null) {
        createMapDiv(album)
      }
    }
    on("data") { e ->
      js("if (e.sourceId !== 'photos' || !e.isSourceLoaded) return;")
      on("move") { updateMarkers("photos", ::createMarker) }
      on("moveend") { updateMarkers("photos", ::createMarker) }
      updateMarkers("photos", ::createMarker)
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
  val div = document.createElement("div").apply {
    id = "XXXXX"
  }
  document.body?.appendChild(div)
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
  val photo = feature.properties["photo"].unsafeCast<Photo>()
  val albumKey = feature.properties["albumKey"].unsafeCast<String>()
//  renderComposable(markerThumb) {
//    val width = min(markerPhotoSize, photo.width * markerPhotoSize / photo.height)
//    val height = min(markerPhotoSize, photo.height * markerPhotoSize / photo.width)
//    Img(
//      attrs = {
//        classes(AppStyle.thumb)
//        style {
//          width(width.px)
//          height(height.px)
//        }
//      },
//      src = photo.thumbnailUrl,
//      alt = photo.description
//    )
//  }
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
              set("photo", it.toJsPhoto())
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
    clusterMaxZoom = 14.0,
    clusterRadius = 50.0,
    data = geoData,
  )
  return source
}

@JsPlainObject
external interface JsPhoto {
  var id: Int
  var directory: String
  var filename: String
  var description: String
  var width: Int
  var height: Int
  var epochSeconds: Long
  var timeOffset: String
  var location: LngLat?
}

private fun Photo.toJsPhoto(): JsPhoto {
  val opt = this
  return jso {
    opt.id.let { id = it }
    opt.filename.let { filename = it }
    opt.description.let { description = it }
    opt.width.let { width = it }
    opt.height.let { height = it }
    opt.location?.let { location = LngLat(it.longitude, it.latitude) } // not needed?
  }
}

private fun createClusterLayer(): SourceLayerSpecification {
  // This works
  return jso<CircleLayerSpecification> {
    id = "clusters"
    type = LayerType.Circle
    source = "photos"
    filter = arrayOf("has", "point_count")
    paint = jso {
      circleColor = arrayOf("step", arrayOf("get", "point_count"), "#51bbd6", 100, "#f1f075", 750, "#f28cb1")
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

//  val layout = SymbolLayoutConfig(
//    textField = "{point_count_abbreviated}",
//    textFont = arrayOf("Arial Unicode MS Bold"),
//    textSize = 12
//  )
//  return SymbolLayerSpecification(
//    id = "cluster-count",
//    type = LayerType.Symbol,
//    source = "photos",
//    filter = arrayOf("has", "point_count"),
//    layout = layout
//  )
}

private fun createUnclusteredLayer(): SourceLayerSpecification {
  return CircleLayerSpecification(
    id = "unclustered-photos",
    type = LayerType.Circle,
    source = "photos",
    filter = arrayOf("!", arrayOf("has", "point_count")),
    //filter = arrayOf("!=", "cluster", true),
    paint = jso<CirclePaintConfig> {
      circleColor = "#11b4da"
      circleRadius = 4
      circleStrokeWidth = 1
      circleStrokeColor = "#FFFFFF"
    }
  )
}