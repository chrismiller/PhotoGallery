package net.redyeti.gallery.web.style

import org.jetbrains.compose.web.css.*

object TextStyle : StyleSheet(AppStyle) {
  val titleText by style {
    color(StyleVars.colourGreyLight.value())
    fontSize(50.px)
    property("font-size", 50.px)
    property("letter-spacing", (-1.5).px)
    property("font-weight", 900)
    property("line-height", 58.px)

    property(
      "font-family",
      "Gotham SSm A,Gotham SSm B,system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }

  val albumText by style {
    color(StyleVars.colourGreyLight.value())
    fontSize(24.px)
    property("font-size", 28.px)
    property("letter-spacing", "normal")
    property("font-weight", 300)
    property("line-height", 40.px)

    property(
      "font-family",
      "Gotham SSm A,Gotham SSm B,system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
    )
  }
}