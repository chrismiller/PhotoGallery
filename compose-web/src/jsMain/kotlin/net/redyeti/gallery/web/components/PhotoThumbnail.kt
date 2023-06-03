package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

@Composable
fun PhotoThumbnail(
  imageUrl: String,
  to: String,
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
        NavLink(attrs = { classes(AppStyle.overlay) }, to = to)
        Div(attrs = { classes(AppStyle.interactionBar) }) {
          content()
        }
      }
    }
  }
}