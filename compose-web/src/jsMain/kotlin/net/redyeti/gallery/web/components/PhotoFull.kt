package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

@Composable
fun PhotoFull(album: Album, photo: Photo) {
  NavLink(to = "/album/${album.id}") {
    Text("<- Back to album")
  }
  Img(
    src = "/image/${album.directory}/large/${photo.filename}",
    alt = photo.description,
    attrs = {
      style {
        width(100.percent)
        height(100.percent)
      }
    }
  )
}