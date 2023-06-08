package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import kotlinx.browser.window
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.Preloader
import net.redyeti.gallery.web.sizedSVG
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.events.Event

data class Count(val current: Int, val total: Int)

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun LightboxImage(
  photo: Photo,
  imageUrl: String,
  caption: @Composable () -> Unit
) {
  Div(attrs = { classes(LightboxStyle.figure) }) {
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

      var loaded by remember { mutableStateOf(false) }
      Preloader.imgPreload(imageUrl) { loaded = true }

      if (!loaded) {
        Div {
          Img(src = sizedSVG(photo.width, photo.height), attrs = {
            classes(LightboxStyle.image)
            // The following line prevents the image from exceeding the window height
            style { maxHeight(maxHeight.px) }
          })
          Span(attrs = {
            style {
              position(Position.Absolute)
              left(50.percent)
              top(50.percent)
              transform { translate((-50).percent, (-50).percent) }
            }
          }) {
            Img(src = "/loading.svg")
          }
        }
      } else {
        Img(src = imageUrl, attrs = {
          classes(LightboxStyle.image)
          // The following line prevents the image from exceeding the window height
          style { maxHeight(maxHeight.px) }
        })
      }

      FigCaption {
        caption()
      }
    }
  }
}

@Composable
fun GalleryCaption(location: GpsCoordinates?, count: Count?, caption: @Composable () -> Unit) {
  Div(attrs = { classes(LightboxStyle.bottomBar) }) {
    ImageDescription(caption)
    AlbumCounter(count)
    GpsLink(location)
  }
}

@Composable
fun ImageDescription(caption: @Composable () -> Unit) {
  Div(attrs = { classes(LightboxStyle.title) }) {
    caption()
  }
}

@Composable
fun AlbumCounter(count: Count?) {
  if (count != null) {
    Div(attrs = { classes(LightboxStyle.counter) }) {
      Text("${count.current + 1} of ${count.total}")
    }
  }
}
