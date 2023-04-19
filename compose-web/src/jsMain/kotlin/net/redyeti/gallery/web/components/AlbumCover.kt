package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.*

private val monthNames = listOf(
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December"
)

val Album.monthName: String
  get() = monthNames[month - 1]

@Composable
fun AlbumCover(album: Album) {
  Div(attrs = { classes(AppStyle.divFloat) }) {
    NavLink(to = "/album/${album.id}") {
      Img(
        src = "/image/${album.directory}/thumb/${album.coverImage}",
        alt = album.name
      )
      Br {}
      P {
        B { Text(album.name) }
        Br {}
        Text("${album.monthName} ${album.year}")
      }
    }
  }
}