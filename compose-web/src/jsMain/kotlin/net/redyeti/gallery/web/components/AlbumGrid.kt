package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.layout.AlbumLayout
import net.redyeti.gallery.layout.LayoutConfig
import net.redyeti.gallery.layout.LayoutData
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
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
        NavOnlyLink(to = "/album/${album.album.id}/${photo.id}") {
          PhotoThumbnail(imageUrl = album.album.thumbnailUrl(photo)) {
            Div(attrs = { classes(AppStyle.thumbText) }) {
              Div(attrs = { classes(AppStyle.truncatedText, AppStyle.thumbSubTitle) }) {
                Text(photo.description)
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