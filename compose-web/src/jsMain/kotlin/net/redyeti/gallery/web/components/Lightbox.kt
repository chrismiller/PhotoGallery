package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.Div

@Composable
fun Lightbox(
  previous: @Composable () -> Unit,
  next: @Composable () -> Unit,
  close: () -> Unit,
  content: @Composable () -> Unit
) {
  Div(attrs = { classes(LightboxStyle.background, LightboxStyle.ready) }) {}
  Div(attrs = { classes(LightboxStyle.popup, LightboxStyle.closeButtonIn, LightboxStyle.ready) }) {
    Div(attrs = {
      classes(LightboxStyle.container, LightboxStyle.imageHolder)
      onClick { e ->
        // Only close if we haven't clicked on an arrow or any other higher level item
        if (e.target == e.currentTarget) {
          close()
        }
      }
    }) {
      Div(attrs = { classes(LightboxStyle.content) }) {
        content()
      }
      previous()
      next()
    }
  }
}

fun sizedSVG(width: Int, height: Int): String {
  return "data:image/svg+xml,%3Csvg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 $width $height\"%3E%3C/svg%3E"
}
