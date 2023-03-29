package net.redyeti.gallery.web.style

import org.jetbrains.compose.web.css.*

object AppCSSVariables {
  val wtColorGreyLight by variable<CSSColorValue>()
  val wtColorGreyDark by variable<CSSColorValue>()

  val wtOffsetTopUnit by variable<CSSUnitValue>()
  val wtHorizontalLayoutGutter by variable<CSSUnitValue>()
  val wtFlowUnit by variable<CSSUnitValue>()

  val wtHeroFontSize by variable<CSSUnitValue>()
  val wtHeroLineHeight by variable<CSSUnitValue>()
  val wtSubtitle2FontSize by variable<CSSUnitValue>()
  val wtSubtitle2LineHeight by variable<CSSUnitValue>()
  val wtH2FontSize by variable<CSSUnitValue>()
  val wtH2LineHeight by variable<CSSUnitValue>()
  val wtH3FontSize by variable<CSSUnitValue>()
  val wtH3LineHeight by variable<CSSUnitValue>()

  val wtColCount by variable<StylePropertyNumber>()
}

object AppStyleSheet : StyleSheet() {
  init {
    "label, a, button" style {
      property(
        "font-family",
        "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
      )
    }

    universal style {
      AppCSSVariables.wtColorGreyLight(Color("#f4f4f4"))
      AppCSSVariables.wtColorGreyDark(Color("#323236"))
      AppCSSVariables.wtOffsetTopUnit(24.px)

      margin(0.px)
    }
  }
}