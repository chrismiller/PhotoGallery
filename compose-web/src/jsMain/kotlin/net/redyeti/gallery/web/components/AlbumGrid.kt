package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.layout.AlbumLayout
import net.redyeti.gallery.layout.LayoutConfig
import net.redyeti.gallery.layout.LayoutData
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun AlbumGrid(album: PopulatedAlbum, albumWidth: Int) {
  val layout = AlbumLayout.compute(
    LayoutConfig(albumWidth, targetRowHeight = 150, tolerance = 0.2),
    LayoutData(),
    album.photos.map { p -> p.width.toDouble() / p.height.toDouble() }
  )
  Div(attrs = {
    classes(AppStyle.photoGrid)
    style {
      height(layout.containerHeight.px)
      width(albumWidth.px)
    }
  }) {
    layout.boxes.forEachIndexed { i, box ->
      val photo = album.photos[i]
      Div(attrs = {
        classes(AppStyle.photoGridCell)
        style {
          left(box.left.px)
          top(box.top.px)
          width(box.width.px)
          height(box.height.px)
        }
      }) {
        Div(attrs = { classes(AppStyle.thumbnailContainer) }) {
          Img(
            attrs = {
              classes(AppStyle.thumb)
              attr("loading", "lazy")
            },
            src = album.album.thumbnailUrl(photo),
            alt = photo.description
          )
          Div(attrs = { classes(AppStyle.interactionView) }) {
            Div(attrs = { classes(AppStyle.photoInteraction) }) {
              NavLink(attrs = { classes(AppStyle.overlay) }, to = "/album/${album.album.id}/${photo.id}")
              Div(attrs = { classes(AppStyle.interactionBar) }) {
                Div(attrs = { classes(AppStyle.interactionItem) }) {
                  Span(attrs = { classes(AppStyle.truncatedText) }) {
                    Text(photo.timeTaken)
                  }
                }
                Div(attrs = { classes(AppStyle.interactionItem) }) {
                  GpsLink(photo.location)
                }
              }
            }
          }
        }
      }
    }
  }
}