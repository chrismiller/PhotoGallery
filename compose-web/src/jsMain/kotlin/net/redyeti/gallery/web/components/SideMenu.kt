package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

enum class SideMenuItem { album, map, maplibre }

@Composable
fun SideMenu(albumKey: String, selectedItem: SideMenuItem) {
  Div(attrs = { classes(AppStyle.sideMenu) }) {
    NavLink(to = "/") {
      Img(
        attrs = { classes(AppStyle.sideImg) },
        src = "/home.svg", alt = "Album Index"
      )
    }

    NavLink(to = "/album/$albumKey") {
      Img(
        attrs = {
          classes(
            if (selectedItem == SideMenuItem.album)
              AppStyle.sideImageSelected
            else
              AppStyle.sideImg
          )
        },
        src = "/gallery.svg", alt = "Photo Album View"
      )
    }

    NavLink(to = "/map/$albumKey") {
      Img(
        attrs = {
          classes(
            if (selectedItem == SideMenuItem.map)
              AppStyle.sideImageSelected
            else
              AppStyle.sideImg
          )
        },
        src = "/map.svg", alt = "Map View"
      )
    }

    NavLink(to = "/maplibre/$albumKey") {
      Img(
        attrs = {
          classes(
            if (selectedItem == SideMenuItem.maplibre)
              AppStyle.sideImageSelected
            else
              AppStyle.sideImg
          )
        },
        src = "/map.svg", alt = "Map Libre View"
      )
    }
  }
}