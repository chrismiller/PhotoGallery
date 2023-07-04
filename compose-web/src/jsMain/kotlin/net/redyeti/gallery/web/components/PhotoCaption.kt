package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.LightboxStyle
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

@Composable
fun StyledText(text: String, style: String) {
  Div(attrs = { classes(style) }) {
    Text(text)
  }
}

@Composable
fun CaptionIcons(photo: Photo) {
  Div(attrs = { classes(LightboxStyle.captionIcons) }) {
    GpsLink(photo.location)
    Fullscreen("fs")
  }
}
