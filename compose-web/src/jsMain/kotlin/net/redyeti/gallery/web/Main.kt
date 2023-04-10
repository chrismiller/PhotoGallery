package net.redyeti.gallery.web

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.InternalCoroutinesApi
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.components.AlbumPage
import net.redyeti.gallery.web.components.IndexPage
import net.redyeti.gallery.web.components.PhotoPage
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

private val koin = initKoin(enableNetworkLogs = true).koin

enum class PageState {
  Index, Album, Photo
}

@InternalCoroutinesApi
fun main() {
  val repo = koin.get<PhotoGalleryInterface>()

  renderComposable(rootElementId = "root") {
    var (pageState, setPageState) = remember { mutableStateOf(PageState.Index) }
    Style(AppStyle)

    when (pageState) {
      PageState.Index -> IndexPage(repo, setPageState)
      PageState.Album -> AlbumPage(repo, setPageState)
      PageState.Photo -> PhotoPage(repo, setPageState)
    }
  }
}