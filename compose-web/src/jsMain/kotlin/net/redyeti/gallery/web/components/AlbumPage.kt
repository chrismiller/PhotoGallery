package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Routing
import kotlinx.browser.window
import net.redyeti.gallery.layout.AlbumLayout
import net.redyeti.gallery.layout.LayoutConfig
import net.redyeti.gallery.layout.LayoutData
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.AppStyle.left
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
  var album: PopulatedAlbum? by remember { mutableStateOf(null) }

  int { albumID ->
    LaunchedEffect(albumID) {
      album = repo.fetchAlbum(albumID)
    }
  }

  noMatch {
    album = null
  }

  val popAlbum = album
  if (popAlbum == null) {
    Text("Loading...")
  } else {
    H1(attrs = { classes(TextStyle.titleText) }) {
      Text(popAlbum.album.name)
    }

    val albumWidth = window.innerWidth - 16
    val layout = AlbumLayout.compute(
      LayoutConfig(albumWidth),
      LayoutData(),
      popAlbum.photos.map { p -> p.width.toDouble() / p.height.toDouble() }
    )

    Div(attrs = {
      classes(AppStyle.wrapper)
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
          PhotoThumbnail(popAlbum.album, popAlbum.photos[i])
        }
      }
    }

//    Div(attrs = { classes(AppStyle.divWrapper) }) {
//      popAlbum.photos.forEach { photo ->
//        PhotoThumbnail(popAlbum.album, photo)
//      }
//    }
  }
}