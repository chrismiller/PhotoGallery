package net.redyeti.maplibre

import androidx.compose.runtime.*
import kotlinx.browser.document
import net.redyeti.maplibre.MarkerCache.Companion.markers
import net.redyeti.maplibre.MarkerCache.Companion.markersOnScreen
import net.redyeti.maplibre.jsobject.Map
import net.redyeti.maplibre.jsobject.Marker
import net.redyeti.maplibre.jsobject.NavigationControl
import net.redyeti.maplibre.jsobject.geojson.MapGeoJSONFeature

@Composable
fun LibreMap(options: MapOptions, mapContent: @Composable (Map.() -> Unit)? = null) {
  var map: Map? by remember { mutableStateOf(null) }

  LaunchedEffect(Unit) {
    // <link rel='stylesheet' href='/maplibre-gl.css' />
    val css = document.createElement("link")
    css.setAttribute("rel", "stylesheet")
    css.setAttribute("href", "/maplibre-gl.css")
    document.head?.appendChild(css)

    val jsOptions = options.toJsMapOptions()
    val script = document.createElement("script").apply {
      map = Map(jsOptions).apply {
        addControl(NavigationControl())
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
    val markers = mutableMapOf<String, Marker>()
    val markersOnScreen = mutableMapOf<String, Marker>()
  }
}

fun Map.updateMarkers(source: String, createMarker: (MapGeoJSONFeature) -> Marker) {
  if (true) return
  val newMarkers = mutableMapOf<String, Marker>()
  val features = querySourceFeatures(source)

  // Create an HTML marker for each feature that's on the screen (if it's not in the cache already)
  for (feature in features) {
    val props = feature.properties
    if (!props.containsKey("cluster")) continue
    val id = props["cluster_id"].toString()
    val marker = markers.getOrPut(id) {
      createMarker(feature)
    }
    markers[id] = marker
    if (!markersOnScreen.containsKey(id)) {
      marker.addTo(this)
    }
    markersOnScreen.minus(newMarkers.keys).forEach {
      it.value.remove()
    }
  }
}


private inline fun Map.newComposition(
  parent: CompositionContext,
  noinline content: @Composable () -> Unit
): Composition {
  return Composition(
    MapApplier(), parent
  ).apply {
    setContent(content)
  }
}