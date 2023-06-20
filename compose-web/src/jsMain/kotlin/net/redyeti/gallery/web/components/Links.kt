package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.Router
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.GpsCoordinates
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.HTMLAnchorElement

@Composable
fun GpsLink(location: GpsCoordinates?) {
  if (location != null) {
    val lat = location.latitude
    val long = location.longitude
    A(href = "https://maps.google.com/maps?z=16&q=$lat,$long&ll=$lat,$long", attrs = {
      target(ATarget.Blank)
      onClick {
        it.stopPropagation()
      }
    }) {
      Img(src = "/location.svg", attrs = {
        attr("width", "20px")
        attr("height", "20px")
      })
    }
  }
}

@Composable
fun MapLink(album: Album) {
  NavOnlyLink(to = "/map/${album.id}") {
    Img(src = "/map.svg", attrs = {
      attr("width", "24px")
      attr("height", "24px")
    })
  }
}

@Composable
fun NavOnlyLink(
  to: String,
  attrs: (AttrsScope<HTMLAnchorElement>.(Boolean) -> Unit)? = null,
  router: Router = Router.current,
  hide: Boolean = false,
  content: ContentBuilder<HTMLAnchorElement>? = null
) {
  A(
    href = to,
    attrs = {
      val currentPath = router.currentPath.path
      val selected = if (to == "/") {
        currentPath == to
      } else {
        currentPath.startsWith(to)
      }

      onClick {
        if (it.ctrlKey || it.metaKey) return@onClick
        router.navigate(to, hide)
        it.preventDefault()
        it.stopPropagation()
      }
      attrs?.invoke(this, selected)
    },
    content = content
  )
}