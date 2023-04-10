package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.web.PageState
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.*

@Composable
fun AlbumCover(album: Album, setPageState: (PageState) -> Unit) {
  Div(attrs = { classes(AppStyle.divFloat) }) {
    A(
      href = "/album/${album.id}",
      attrs = { onClick { setPageState(PageState.Album) } }
    ) {
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