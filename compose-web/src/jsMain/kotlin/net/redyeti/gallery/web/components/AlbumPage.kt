package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Router
import app.softwork.routingcompose.Routing
import kotlinx.browser.document
import kotlinx.browser.window
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.web.Preloader.imgPreload
import net.redyeti.gallery.web.style.AppStyle
import net.redyeti.gallery.web.style.LightboxStyle
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Routing
@Composable
fun RouteBuilder.AlbumPage(repo: PhotoGalleryInterface) {
  // 0.9 = adjustment for pageWrapper.padding left+right
  fun albumWidth() = (window.innerWidth * 0.9).roundToInt()

  var album: PopulatedAlbum? by remember { mutableStateOf(null) }
  var albumWidth by remember { mutableStateOf(albumWidth()) }
  var photoID = -1

  int { albumID ->
    int {
      // A photo ID was provided
      photoID = max(0, it)
    }
    noMatch {
      photoID = -1
    }
    LaunchedEffect(albumID) {
      album = repo.fetchAlbum(albumID)
      photoID = min(photoID, album!!.photos.size - 1)
    }

    DisposableEffect(albumWidth) {
      val resizeListener: (Event) -> Unit = {
        albumWidth = albumWidth()
      }
      window.addEventListener("resize", resizeListener)
      onDispose {
        window.removeEventListener("resize", resizeListener)
      }
    }
  }

  noMatch {
    album = null
  }

  val popAlbum = album
  if (popAlbum == null) {
    Div(attrs = { classes(AppStyle.loader) }) {}
  } else {
    Page(popAlbum.album.title, popAlbum.album.subtitle) {
      AlbumGrid(popAlbum, albumWidth)
    }

    if (photoID >= 0) {
      PhotoPopup(popAlbum, photoID, "/album")
    }
  }
}

@Composable
fun PhotoPopup(popAlbum: PopulatedAlbum, photoID: Int, base: String) {
  var id by remember { mutableStateOf(photoID) }

  val router = Router.current

  val prevID = popAlbum.wrappedID(id - 1)
  val prevUrl = "$base/${popAlbum.album.id}/$prevID"
  val nextID = popAlbum.wrappedID(id + 1)
  val nextUrl = "$base/${popAlbum.album.id}/$nextID"
  val close = { router.navigate("$base/${popAlbum.album.id}") }

  DisposableEffect(id) {
    document.onkeydown = { e ->
      when (e.key) {
        "Escape" -> {
          close()
        }
        "ArrowLeft" -> {
          router.navigate(prevUrl)
          id = prevID
        }
        "ArrowRight" -> {
          router.navigate(nextUrl)
          id = nextID
        }
      }
    }
    onDispose {
      document.onkeydown = null
    }
  }

  Lightbox(
    close = close,
    previous = {
      NavLink(prevUrl) {
        Button(attrs = {
          title("Previous (Left arrow key)")
          classes(LightboxStyle.arrow, LightboxStyle.arrowLeft)
          onClick { id = prevID }
        }
        ) {}
      }
    },
    next = {
      NavLink(nextUrl) {
        Button(attrs = {
          title("Next (Right arrow key)")
          classes(LightboxStyle.arrow, LightboxStyle.arrowRight)
          onClick { id = nextID }
        }
        ) {}
      }
    }
  ) {

    // Preload a few adjacent images, so they can be displayed quickly when needed
    for (i in listOf(-1, 1, 2, 3)) {
      imgPreload(popAlbum.imageUrl(id + i))
    }

    val photo = popAlbum.photos[id]
    LightboxImage(
      photo,
      popAlbum.imageUrl(id)
    ) {
      GalleryCaption(photo.location, Count(id, popAlbum.photos.size)) {
        Text(photo.description)
        Small {
          Text("Copyright © Chris Miller")
        }
      }
    }
  }
}
