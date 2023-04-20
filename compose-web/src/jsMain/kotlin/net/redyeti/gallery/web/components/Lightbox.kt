package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.Routing
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Routing
@Composable
fun Lightbox(content: @Composable () -> Unit) {
  Div(attrs = {
    classes(AppStyle.lightbox)
    id("lightbox")
  }) {
    Div(attrs = {
      classes(AppStyle.lightboxInner)
    }) {
      Button(attrs = {
        classes(AppStyle.lightboxClose)
      }) {
        Text("×")
      }
      content()
    }
  }
}