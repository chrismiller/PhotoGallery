package net.redyeti.maplibre

import androidx.compose.runtime.*
import kotlinx.browser.document
import net.redyeti.maplibre.jsobject.Map

@Composable
fun LibreMap(options: MapOptions) {
  var map: Map? by remember { mutableStateOf(null) }

  LaunchedEffect(Unit) {
    val jsOptions = options.toJsMapOptions()
    val script = document.createElement("script").apply {
      map = Map(jsOptions)
    }
    document.head?.appendChild(script)
  }
}