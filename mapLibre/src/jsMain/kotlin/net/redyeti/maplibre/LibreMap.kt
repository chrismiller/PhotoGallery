package net.redyeti.maplibre

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.maplibre.jsobject.MapOptions

@Composable
fun LibreMap(id: String, options: MapOptions, content: @Composable (() -> Unit) = {}) {
  var map: net.redyeti.maplibre.jsobject.Map? by remember { mutableStateOf(null) }

//  LaunchedEffect(Unit) {
//    val script = document.createElement("script").apply {
//      val src = StringBuilder("https://maps.googleapis.com/maps/api/js?")
//      src.append("&callback=initMap")
//      // extra?.let { src.append("&$it") }
//      this.asDynamic().src = src
//      this.asDynamic().async = true
//    }
//    document.head?.appendChild(script)
//  }

  window.asDynamic().initMap = {
    map = newMap(id, options)
  }

  content()
}

internal fun newMap(id: String, options: MapOptions): net.redyeti.maplibre.jsobject.Map {
  return js("new Map(document.getElementById(id), options);") as net.redyeti.maplibre.jsobject.Map
}