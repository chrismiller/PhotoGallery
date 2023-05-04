package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.*

@Composable
fun AppHeader(title: String, subtitle: String) {
  Div(attrs = { classes(AppStyle.headerText) }) {
    H1 { Text(title) }
    H2 { Text(subtitle) }
  }
  Div(attrs = { classes(AppStyle.headerLogo) }) {
    Img(attrs = { style { display(DisplayStyle.Inherit) } }, src = "/logo.png", alt = "Red Yeti Logo")
  }
}