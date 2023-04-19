package net.redyeti.gallery.web

import app.softwork.routingcompose.BrowserRouter
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.components.AlbumPage
import net.redyeti.gallery.web.components.IndexPage
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposableInBody

private val koin = initKoin(enableNetworkLogs = true).koin

fun main() {
  val repo = koin.get<PhotoGalleryInterface>()

  renderComposableInBody {
    Style(AppStyle)
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