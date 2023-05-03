package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
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

data class Count(val current: Int, val total: Int)

@Composable
fun LightboxImage(imageUrl: String, count: Count? = null, close: () -> Unit, caption: @Composable () -> Unit) {
  Div(attrs = { classes(LightboxStyle.figure) }) {
    Button(attrs = {
      classes(LightboxStyle.close)
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
        classes(LightboxStyle.image)
        // The following line prevents the image from exceeding the window height
        style { maxHeight(maxHeight.px) }
      })
      FigCaption {
        Div(attrs = { classes(LightboxStyle.bottomBar) }) {
          Div(attrs = { classes(LightboxStyle.title) }) {
            caption()
          }
          if (count != null) {
            Div(attrs = { classes(LightboxStyle.counter) }) {
              Text("${count.current} of ${count.total}")
            }
          }
        }
      }
    }
  }
}