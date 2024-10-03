package net.redyeti.maplibre

import androidx.compose.runtime.*
import kotlinx.browser.document
import net.redyeti.maplibre.jsobject.Map
import net.redyeti.maplibre.jsobject.NavigationControl

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
      map = Map(jsOptions)
      map!!.addControl(NavigationControl())
    }
    document.head?.appendChild(script)
  }

  val parentComposition = rememberCompositionContext()
  val currentContent by rememberUpdatedState(mapContent)
  LaunchedEffect(map) {
    val currentMap = map
    if (currentMap != null) {
      currentMap.newComposition(parentComposition) {
        currentContent?.invoke(currentMap)
      }
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