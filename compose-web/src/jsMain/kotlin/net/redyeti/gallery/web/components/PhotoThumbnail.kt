package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Img

@Composable
fun PhotoThumbnail(album: Album, photo: Photo) {
  NavLink(to = "/album/${album.id}/${photo.id}") {
    Img(
      attrs = { classes(AppStyle.thumb) },
      src = album.thumbnailUrl(photo),
      alt = photo.description
    )
  }
}