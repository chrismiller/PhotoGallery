package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun PhotoCaption(photo: Photo, count: Count?) {
  Div(attrs = { classes(LightboxStyle.lightboxCaption) }) {
    Div(attrs = { classes(LightboxStyle.textArea) }) {
      StyledText(photo.description, LightboxStyle.captionText)
      StyledText("Copyright © Chris Miller", LightboxStyle.copyrightText)
      if (count != null) {
        StyledText("${count.current + 1} of ${count.total}", LightboxStyle.captionCounter)
      }
    }
    CaptionIcons(photo)
  }
}

fun nextUrl(text: String, start: Int): Pair<Int, Int>? {
  val i = text.indexOf("http", start)
  if (i < 0
    || (!text.startsWith("://", i + 4)
        && !text.startsWith("s://", i + 4))
  ) {
    return null
  }
  var end = text.indexOf(' ', i + 6)
  if (end < 0) end = text.length
  while (end > i + 6 && text[end - 1] == '.') { end-- }
  return Pair(i, end)
}

@Composable
fun StyledText(text: String, style: String) {
  Div(attrs = { classes(style) }) {
    var start = 0
    val end = text.length
    while (start < end) {
      val url = nextUrl(text, start)
      start = if (url == null) {
        Text(text.substring(start, end))
        end
      } else {
        Text(text.substring(start, url.first))
        val href = text.substring(url.first, url.second)
        A(href = href) { Text(href) }
        url.second
      }
    }
  }
}

@Composable
fun CaptionIcons(photo: Photo) {
  Div(attrs = { classes(LightboxStyle.captionIcons) }) {
    GpsLink(photo.location)
    Fullscreen("fs")
  }
}
