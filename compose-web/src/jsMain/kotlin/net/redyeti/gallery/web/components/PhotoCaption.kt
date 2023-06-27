package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text

@Composable
fun PhotoCaption(photo: Photo, count: Count?) {
  Div(attrs = { classes(LightboxStyle.lightboxCaption) }) {
    Div(attrs = { classes(LightboxStyle.captionText) }) {
      Text(photo.description)
      Small {
        Text("Copyright © Chris Miller")
      }
    }
    AlbumCounter(count)
    CaptionIcons(photo)
  }
}

@Composable
fun AlbumCounter(count: Count?) {
  if (count != null) {
    Div(attrs = { classes(LightboxStyle.captionCounter) }) {
      Text("${count.current + 1} of ${count.total}")
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
