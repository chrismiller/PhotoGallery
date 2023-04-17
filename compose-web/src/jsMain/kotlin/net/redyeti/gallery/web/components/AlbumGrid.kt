package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.layout.AlbumLayout
import net.redyeti.gallery.layout.ContainerPadding
import net.redyeti.gallery.layout.LayoutConfig
import net.redyeti.gallery.layout.LayoutData
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun AlbumGrid(album: PopulatedAlbum, albumWidth: Int) {
  val layout = AlbumLayout.compute(
    LayoutConfig(
      albumWidth, padding = ContainerPadding(30, 50, 50, 50),
      targetRowHeight = 150, tolerance = 0.2
    ),
    LayoutData(),
    album.photos.map { p -> p.width.toDouble() / p.height.toDouble() }
  )
  Div(attrs = {
    classes(AppStyle.justified)
    style {
      height(layout.containerHeight.px)
      width(albumWidth.px)
    }
  }) {
    layout.boxes.forEachIndexed { i, box ->
      Div(attrs = {
        classes(AppStyle.box)
        style {
          left(box.left.px)
          top(box.top.px)
          width(box.width.px)
          height(box.height.px)
        }
      }) {
        PhotoThumbnail(album.album, album.photos[i])
      }
    }
  }
}