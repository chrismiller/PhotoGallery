package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun PhotoCaption(photo: Photo, count: Count?, updateId: (Int) -> Unit) {
  Div(attrs = { classes(LightboxStyle.lightboxCaption) }) {
    Div(attrs = { classes(LightboxStyle.textArea) }) {
      StyledText(photo.description, LightboxStyle.captionText) { id ->
        updateId(id)
      }
      val copyright = if (photo.directory == "AlgeriaArches") {
        "Copyright © {url:https://naturalarches.org/tassili/}naturalarches.org{/}"
      } else "Copyright © Chris Miller"
      StyledText(copyright, LightboxStyle.copyrightText)
    }
    if (count != null) {
      StyledText("${count.current + 1} of ${count.total}", LightboxStyle.captionCounter)
    }
  }
  CaptionIcons(photo)
}

@Composable
fun StyledText(text: String, style: String? = null, updateId: (Int) -> Unit = {}) {
  Div(attrs = { if (style != null) classes(style) }) {
    TextWithLinks(text, updateId)
  }
}

// This matches any {type:data}content{/} templated text in the description.
// The extra escaping is required, otherwise the regex doesn't work correctly with Javascript
private val replaceRegex = "\\{(\\w+):(.+?)\\}(.+?)\\{\\/\\}".toRegex()

private fun mapUrl(lat: String, lon: String) = "https://maps.google.com/maps?z=16&q=$lat,$lon&ll=$lat,$lon"

@Composable
fun TextWithLinks(text: String, updateId: (Int) -> Unit) {
  var start = 0
  replaceRegex.findAll(text).forEach { match ->
    Text(text.substring(start, match.range.first))
    start = match.range.last + 1
    val type = match.groups[1]!!.value
    val data = match.groups[2]!!.value
    val content = match.groups[3]!!.value
    when (type) {
      "photo" -> A(attrs = { onClick { updateId(data.toInt()) } }) { Text(content) }
      "map" -> {
        val (lat, lon) = data.split(",")
        A(href = mapUrl(lat, lon)) { Text(content) }
      }
      "url" -> A(href = data) { Text(content) }
      else -> Text(content)
    }
  }
  if (start < text.length) {
    Text(text.substring(start))
  }
}

@Composable
fun CaptionIcons(photo: Photo) {
  Div(attrs = { classes(LightboxStyle.captionIcons) }) {
    GpsLink(photo.location)
    Fullscreen("fs")
  }
}
