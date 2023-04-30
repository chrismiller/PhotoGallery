package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import kotlinx.browser.window
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event

@Composable
fun Lightbox(
  previous: @Composable () -> Unit,
  next: @Composable () -> Unit,
  content: @Composable () -> Unit
) {
  Div(attrs = { classes(LightboxStyle.lbBackground, LightboxStyle.lbReady) }) {}
  Div(attrs = { classes(LightboxStyle.lbPopup, LightboxStyle.lbCloseButtonIn, LightboxStyle.lbReady) }) {
    Div(attrs = { classes(LightboxStyle.lbContainer, LightboxStyle.lbImageHolder) }) {
      Div(attrs = { classes(LightboxStyle.lbContent) }) {
        content()
      }
      previous()
      next()
    }
  }
}

data class Count(val current: Int, val total: Int)

@Composable
fun LightboxImage(imageUrl: String, count: Count? = null, close: () -> Unit, caption: @Composable () -> Unit) {
  Div(attrs = { classes(LightboxStyle.lbFigure) }) {
    Button(attrs = {
      classes(LightboxStyle.lbClose)
      title("Close (Esc)")
      type(ButtonType.Button)
      onClick { close() }
    }) { Text("×") }
    Figure {
      var maxHeight by remember { mutableStateOf(window.innerHeight) }

      DisposableEffect(maxHeight) {
        val resizeListener: (Event) -> Unit = {
          maxHeight = window.innerHeight
        }
        window.addEventListener("resize", resizeListener)
        onDispose {
          window.removeEventListener("resize", resizeListener)
        }
      }
      Img(src = imageUrl, attrs = {
        classes(LightboxStyle.lbImage)
        // The following line prevents the image from exceeding the window height
        style { maxHeight(maxHeight.px) }
      })
      FigCaption {
        Div(attrs = { classes(LightboxStyle.lbBottomBar) }) {
          Div(attrs = { classes(LightboxStyle.lbTitle) }) {
            caption()
          }
          Div(attrs = { classes(LightboxStyle.lbCounter) }) {
            if (count != null) {
              Text("${count.current} of ${count.total}")
            }
          }
        }
      }
    }
  }
}