package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.*

@Composable
fun AppHeader(title: String, subtitle: String) {
  Div(attrs = { classes(AppStyle.headerText) }) {
    H1 { Text(title) }
    H2 { StyledText(subtitle) }
  }
  Div(attrs = { classes(AppStyle.headerLogo) }) {
    NavLink(to = "/") {
      Img(attrs = { style { display(DisplayStyle.Inherit) } }, src = "/logo.png", alt = "Red Yeti Logo")
    }
  }
}