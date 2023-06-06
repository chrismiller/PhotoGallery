package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

@Composable
fun PhotoThumbnail(
  imageUrl: String,
  content: @Composable () -> Unit
) {
  Div(attrs = { classes(AppStyle.thumbnailContainer) }) {
    Img(
      attrs = {
        classes(AppStyle.thumb)
        attr("loading", "lazy")
      },
      src = imageUrl
    )
    Div(attrs = { classes(AppStyle.interactionView) }) {
      Div(attrs = { classes(AppStyle.photoInteraction) }) {
        Div(attrs = { classes(AppStyle.interactionBar) }) {
          content()
        }
      }
    }
  }
}