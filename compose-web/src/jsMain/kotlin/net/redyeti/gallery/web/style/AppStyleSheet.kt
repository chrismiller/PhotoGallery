package net.redyeti.gallery.web.style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto

object StyleVars {
  val colourGreyLight by variable<CSSColorValue>()
  val colourGreyMedium by variable<CSSColorValue>()
  val colourGreyDark by variable<CSSColorValue>()

  val imageSideMargin by variable<CSSUnitValue>()
  val imageBorder by variable<CSSBorder>()

  val heroFontSize by variable<CSSUnitValue>()
  val heroLineHeight by variable<CSSUnitValue>()
  val subtitle2FontSize by variable<CSSUnitValue>()
  val subtitle2LineHeight by variable<CSSUnitValue>()
  val h2FontSize by variable<CSSUnitValue>()
  val h2LineHeight by variable<CSSUnitValue>()
  val h3FontSize by variable<CSSUnitValue>()
  val h3LineHeight by variable<CSSUnitValue>()

  val colCount by variable<StylePropertyNumber>()
}

object AppStyleSheet : StyleSheet() {
  init {
    "body" style {
      fontFamily("Helvetica", "Geneva", "Arial", "sans-serif")
      color(StyleVars.colourGreyMedium.value())
      backgroundColor(Color.black)
      textAlign("center")
    }

    "img" style {
      border(1.px, LineStyle.Solid, Color.white)
      marginLeft(StyleVars.imageSideMargin.value())
      marginRight(StyleVars.imageSideMargin.value())

      "logo" style {
        border(0.px)
      }
    }

    "a" style {
      color(StyleVars.colourGreyMedium.value())
      textDecoration("none")
      textAlign("center")
      hover {
        color(Color.white)
      }
    }

    universal style {
      StyleVars.colourGreyLight(Color("#f4f4f4"))
      StyleVars.colourGreyMedium(Color("#b3b3b3"))
      StyleVars.colourGreyDark(Color("#323236"))
      StyleVars.imageSideMargin(20.px)
      margin(0.px)
    }
  }

  val divWrapper by style {
    width(880.px)
    // margin 0.px auto
  }

  val divHeader by style {
    width(880.px)
    // margin 0.px auto
    overflow("auto")
    property("clear", "both")
  }

  val divFloat by style {
    property("float", "left")
  }
}