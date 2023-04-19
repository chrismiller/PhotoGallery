package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Routing
import kotlinx.browser.window
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun PhotoLightbox(album: PopulatedAlbum, photo: Photo) {
  val lightboxWidth = window.innerWidth * 0.9
  val lightboxHeight = window.innerHeight * 0.9
  Div(attrs = {
    classes(AppStyle.lightbox)
    style {
      width(lightboxWidth.px)
      height(lightboxHeight.px)
    }
  }) {
    NavLink(to = "/album/${album.album.id}") {
      Text("<- Back to album")
    }
    Img(
      src = "/image/${album.album.directory}/large/${photo.filename}",
      alt = photo.description,
      attrs = {
        style {
          maxWidth((lightboxWidth * 0.95).px)
          maxHeight((lightboxHeight * 0.95).px)
        }
      }
    )
  }
}