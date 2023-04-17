package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Img

@Composable
fun PhotoThumbnail(album: Album, photo: Photo) {
  NavLink(to = "/photo/${album.id}/${photo.id}") {
    Img(
      src = "/image/${album.directory}/thumb/${photo.filename}",
      alt = photo.description,
      attrs = {
        style {
          width(100.percent)
          height(100.percent)
        }
      }
    )
  }
}