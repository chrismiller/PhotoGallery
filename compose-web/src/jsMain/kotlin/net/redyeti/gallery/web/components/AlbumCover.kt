package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.*

@Composable
fun AlbumCover(album: Album) {
  Div(attrs = { classes(AppStyle.albumCover) }) {
    NavLink(to = "/album/${album.id}") {
      Img(
        attrs = { classes(AppStyle.thumb) },
        src = "/image/${album.directory}/thumb/${album.coverImage}",
        alt = album.title
      )
      Br()
      B { Text(album.title) }
      Br()
      Small { Text(album.subtitle) }
    }
  }
}