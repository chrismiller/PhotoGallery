package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.*

@Composable
fun PhotoThumbnail(album: Album, photo: Photo) {
  Div(attrs = { classes(AppStyle.divFloat) }) {
    NavLink(to = "/photo/${album.id}/${photo.id}") {
      Img(
        src = "/image/${album.directory}/thumb/${photo.filename}",
        alt = photo.description
      )
    }
  }
}