package net.redyeti.maplibre

import androidx.compose.runtime.*
import kotlinx.browser.document
import net.redyeti.maplibre.MarkerCache.Companion.markers
import net.redyeti.maplibre.MarkerCache.Companion.markersOnScreen
import net.redyeti.maplibre.jsobject.Map
import net.redyeti.maplibre.jsobject.Marker
import net.redyeti.maplibre.jsobject.geojson.MapGeoJSONFeature
import net.redyeti.maplibre.jsobject.ui.control.GeolocateControl
import net.redyeti.maplibre.jsobject.ui.control.NavigationControl
import net.redyeti.maplibre.jsobject.ui.control.ScaleControl
import org.w3c.dom.HTMLElement
import kotlin.math.min

@Composable
fun LibreMap(options: MapOptions, mapContent: @Composable (Map.() -> Unit)? = null) {
  var map: Map? by remember { mutableStateOf(null) }

  LaunchedEffect(Unit) {
    val css = document.createElement("link")
    css.setAttribute("rel", "stylesheet")
    css.setAttribute("href", "/maplibre-gl-5.0.0-pre.10.css")
    //css.setAttribute("href", "/maplibre-gl-4.7.1.css")
    document.head?.appendChild(css)

    val jsOptions = options.toJsMapOptions()
    val script = document.createElement("script").apply {
      map = Map(jsOptions).apply {
        addControl(NavigationControl())
        addControl(ScaleControl())
        addControl(GeolocateControl())
      }
    }
    document.head?.appendChild(script)
  }

  val parentComposition = rememberCompositionContext()
  val currentContent by rememberUpdatedState(mapContent)
  LaunchedEffect(map) {
    val currentMap = map
    currentMap?.newComposition(parentComposition) {
      currentContent?.invoke(currentMap)
    }
  }
}

class MarkerCache {
  companion object {
    // TODO: should be limit the number of markers we remember?
    val markers = mutableMapOf<Int, Marker>()
    var markersOnScreen = mutableMapOf<Int, Marker>()
  }
}

fun Map.resetCache() {
  markers.forEach { it.value.remove() }
  markers.clear()
  markersOnScreen.clear()
}

fun Map.updateMarkers(source: String, createMarker: (MapGeoJSONFeature) -> Marker, fadeStart: Double = 12.0, fadeStop: Double = 13.0) {
  // This logic takes a similar approach to the code in https://maplibre.org/maplibre-gl-js/docs/examples/cluster-html/
  if (getZoom() < fadeStart) {
    // Remove any thumbnails, we're zoomed out too far
    markersOnScreen.forEach { it.value.remove() }
    markersOnScreen.clear()
    return
  }

  // Apply some transparency to the marker if we're between the fade zoom levels
  val opacity = min((getZoom() - fadeStart) / (fadeStop - fadeStart), 1.0).toString()
  // Scale the thumbnails from 10% to 100% as we zoom in
  val scaleFactor = (min((getZoom() - fadeStart) / (17.0 - fadeStart), 1.0) * 0.9 + 0.1).toString()

  // Create an HTML marker for each feature that's on the screen (if it's not in the cache already).
  // Note that querySourceFeatures() can be quite expensive when there are lots of features visible.
  val newMarkers = mutableMapOf<Int, Marker>()
  for (feature in querySourceFeatures(source)) {
    val photoId = feature.properties.id.unsafeCast<Int>()
    val marker = markers.getOrPut(photoId) {
      createMarker(feature)
    }
    marker.setOpacity(opacity)
    val img = marker.getElement().firstElementChild
    (img as HTMLElement?)?.style?.setProperty("scale", scaleFactor)
    newMarkers[photoId] = marker
    if (!markersOnScreen.containsKey(photoId)) {
      marker.addTo(this)
    }
  }

  markersOnScreen.forEach { entry ->
    // Remove markers that are no longer visible from the map
    if (!newMarkers.containsKey(entry.key)) {
      entry.value.remove()
    }
  }
  markersOnScreen = newMarkers
}


private inline fun Map.newComposition(
  parent: CompositionContext, noinline content: @Composable () -> Unit
): Composition {
  return Composition(
    MapApplier(), parent
  ).apply {
    setContent(content)
  }
}