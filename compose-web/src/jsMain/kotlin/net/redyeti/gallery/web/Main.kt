package net.redyeti.gallery.web

import app.softwork.routingcompose.BrowserRouter
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.components.AlbumPage
import net.redyeti.gallery.web.components.IndexPage
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.LightboxStyle
import net.redyeti.gallery.web.style.TextStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.stringPresentation
import org.jetbrains.compose.web.renderComposableInBody

private val koin = initKoin(enableNetworkLogs = true).koin

fun main() {
  val repo = koin.get<PhotoGalleryInterface>()

  renderComposableInBody {
// Debugging - output the generated stylesheet
//    LightboxStyle.cssRules.forEach {
//      console.info(it.stringPresentation())
//    }
    Style(AppStyle)
    Style(TextStyle)
    Style(LightboxStyle)
    BrowserRouter("/") {
      route("album") {
        AlbumPage(repo)
      }
      route("") {
        IndexPage(repo)
      }
    }
  }
}