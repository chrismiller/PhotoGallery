package net.redyeti.gallery.web

import app.softwork.routingcompose.BrowserRouter
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.components.*
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.LightboxStyle
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposableInBody

private val koin = initKoin(enableNetworkLogs = true).koin

fun main() {
  val repo = koin.get<PhotoGalleryInterface>()

  renderComposableInBody {
// Debugging - output the generated stylesheet
//    LightboxStyle.cssRules.forEach {
//      console.info(it.stringPresentation())
//    }
//    AppStyle.cssRules.forEach {
//      console.info(it.stringPresentation())
//    }
    Style(AppStyle)
    Style(TextStyle)
    Style(LightboxStyle)

    BrowserRouter("/") {
      route("album") {
        string { albumID ->
          SideMenu(albumID, SideMenuItem.album)
        }
        AlbumPage(repo)
      }
      route("map") {
        string { albumID ->
          SideMenu(albumID, SideMenuItem.map)
        }
        MapPage(repo)
      }
      route("maplibre") {
        string { albumID ->
          SideMenu(albumID, SideMenuItem.maplibre)
        }
        MapLibrePage(repo)
      }
      noMatch {
        IndexPage(repo)
      }
    }
  }
}