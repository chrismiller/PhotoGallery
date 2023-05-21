package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.Routing
import kotlinx.browser.document
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLDivElement
import kotlin.js.Date

@Routing
@Composable
fun Page(
  title: String, subtitle: String,
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
  content: @Composable () -> Unit
) {
  document.title = "Travel Photos - $title"
  Div(attrs = {
    classes(AppStyle.pageWrapper)
    attrs?.invoke(this)
  }) {
    Header {
      AppHeader(title, subtitle)
    }
    Main {
      content()
    }
    Footer {
      val year = Date().getFullYear()
      Text("Copyright © Chris Miller, 2005-$year. All rights reserved.")
    }
  }
}