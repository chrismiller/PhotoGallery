package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import kotlinx.browser.document
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.Div

@Composable
fun Fullscreen(elementId: String) {
  if (document.fullscreenEnabled) {
    Div(attrs = {
      classes(LightboxStyle.fullscreen)
      onClick {
        if (document.fullscreenElement != null) {
          document.exitFullscreen()
        } else {
          document.getElementById(elementId)?.requestFullscreen()
        }
      }
    })
  }
}