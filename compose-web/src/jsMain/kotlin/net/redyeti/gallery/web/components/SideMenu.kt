package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

enum class SideMenuItem { album, map }

@Composable
fun SideMenu(albumID: Int, selectedItem: SideMenuItem) {
  Div(attrs = { classes(AppStyle.sideMenu) }) {
    NavLink(to = "/") {
      Img(
        attrs = { classes(AppStyle.sideImg) },
        src = "/home.svg", alt = "Album Index"
      )
    }

    NavLink(to = "/album/$albumID") {
      Img(
        attrs = {
          classes(
            if (selectedItem == SideMenuItem.map)
              AppStyle.sideImg
            else
              AppStyle.sideImageSelected
          )
        },
        src = "/gallery.svg", alt = "Photo Album View"
      )
    }

    NavLink(to = "/map/$albumID") {
      Img(
        attrs = {
          classes(
            if (selectedItem == SideMenuItem.album)
              AppStyle.sideImg
            else
              AppStyle.sideImageSelected
          )
        },
        src = "/map.svg", alt = "Map View"
      )
    }
  }
}