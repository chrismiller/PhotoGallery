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
  Div(attrs = {
    id("fs")
    classes(LightboxStyle.popup, LightboxStyle.closeButtonIn, LightboxStyle.ready)
  })
  {
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
        Fullscreen("fs")
      }
      previous()
      next()
    }
  }
}