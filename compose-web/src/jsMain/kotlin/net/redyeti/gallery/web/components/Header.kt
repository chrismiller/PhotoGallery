package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.*

@Composable
fun Header(title: String, subtitle: String) {
  Div(attrs = { classes(AppStyle.divHeader) }) {
    Div(attrs = { style { property("float", "left") } }) {
      B { Text(title) }
      Br {}
      Text(subtitle)
    }
    Div(attrs = {
      classes(AppStyle.divHeader)
      style { property("float", "right") } }
    ) {
      Img(attrs = { style { display(DisplayStyle.Inherit) }}, src = "/logo.png", alt = "Red Yeti Logo")
    }
  }
}