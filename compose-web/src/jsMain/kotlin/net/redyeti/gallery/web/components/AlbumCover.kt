package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.web.style.AppStyleSheet
import org.jetbrains.compose.web.dom.*

@Composable
fun AlbumCover(album: Album) {
  Div(attrs = { classes(AppStyleSheet.divFloat) }) {
    A(href = "album/${album.id}") {
      Img(
        src = "/image/${album.directory}/thumb/${album.coverImage}",
        alt = album.name
      )
      Br {}
      P {
        Text(album.name)
      }
    }
  }
}