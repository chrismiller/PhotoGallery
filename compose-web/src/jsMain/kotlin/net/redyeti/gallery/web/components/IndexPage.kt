package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun IndexPage(repo: PhotoGalleryInterface) {
  var albums by remember { mutableStateOf(emptyList<Album>()) }

  LaunchedEffect(true) {
    albums = repo.fetchAlbums()
  }

  Page("Travel Photos", "Chris Miller") {
    Div(attrs = { classes(AppStyle.coverWrapper) }) {
      albums.forEach { album ->
        Div(attrs = { classes(AppStyle.albumCover) }) {
          val albumUrl = "/album/${album.id}"
          PhotoThumbnail(imageUrl = album.thumbnailUrl(album.coverImage), to = albumUrl) {
            NavLink(
              attrs = { classes(AppStyle.thumbText) },
              to = albumUrl
            ) {
              Div(attrs = { classes(AppStyle.truncatedText, AppStyle.thumbSubTitle) }) {
                // TODO: show photo count etc?
              }
            }
            Div(attrs = { classes(AppStyle.interactionItem) }) {
              MapLink(album)
            }
          }
          Div {
            Div(attrs = {
              classes(AppStyle.truncatedText, AppStyle.thumbTitle)
              style { textAlign("center") }
            }) {
              Text(album.title)
            }
            Div(attrs = {
              classes(AppStyle.truncatedText, AppStyle.thumbSubTitle)
              style { textAlign("center") }
            }) {
              Text(album.subtitle)
            }
          }
        }
      }
    }
  }
}