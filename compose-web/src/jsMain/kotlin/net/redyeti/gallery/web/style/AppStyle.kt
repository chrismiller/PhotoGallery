package net.redyeti.gallery.web.style

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import kotlin.time.Duration.Companion.seconds

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

object AppStyle : StyleSheet() {
  init {
    "body" style {
      fontFamily("Source Sans Pro", "sans-serif")
      color(StyleVars.colourGreyLight.value())
      backgroundColor(Color.black)
      textAlign("center")
    }

    "img" style {
      border(1.px, LineStyle.Solid, Color.white)
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
    width(1200.px)
    // margin 0.px auto
    // https://kotlinlang.slack.com/archives/C01F2HV7868/p1665751751246239
    marginTop(0.px)
    marginBottom(0.px)
    property("margin-left", auto)
    property("margin-right", auto)
  }

  val divHeader by style {
    marginTop(0.px)
    marginBottom(0.px)
    property("margin-left", auto)
    property("margin-right", auto)
    overflow("auto")
    property("clear", "both")
  }

  val divFloat by style {
    property("float", "left")
  }

  val justified by style {
    position(Position.Relative)
    backgroundColor(Color.black)
  }

  val wrapper by style {
    position(Position.Relative)
    marginTop(0.px)
    marginBottom(0.px)
    property("margin-left", auto)
    property("margin-right", auto)
    backgroundColor(StyleVars.colourGreyLight.value())
  }

  val box by style {
    position(Position.Absolute)
    backgroundColor(StyleVars.colourGreyMedium.value())
  }

  @OptIn(ExperimentalComposeWebApi::class)
  val lightbox by style {
    position(Position.Fixed)
    display(DisplayStyle.Flex)
    alignItems(AlignItems.Center)
    justifyContent(JustifyContent.Center)
    top(0.px)
    left(0.px)
    width(100.percent)
    height(100.vh)
    backgroundColor(rgba(0, 0, 0, 0.4))
    opacity(0.01) // this prevents a repaint between 0 and 0.01
    this.blank
    transitions {
      opacity(0.3)
      defaultTimingFunction(AnimationTimingFunction.Ease)
      default
    }
    property("z-index", "1000")
    property("will-change", "opacity")

    style(firstChild) {

    }
  }

  @OptIn(ExperimentalComposeWebApi::class)
  val lightboxInner by style {
    maxWidth(100.percent)
    transform { scale(0.9) }
    transitions {
      transform {
        defaultDuration(0.4.s)
        defaultTimingFunction(AnimationTimingFunction.Ease)
      }
    }
    property("z-index", "1")
    property("will-change", "transform")

    "image" style {
      display(DisplayStyle.Block)
      position(Position.Absolute)
      top(0.px)
      left(0.px)
      bottom(0.px)
      right(0.px)
      property("margin", auto)
      maxWidth(95.percent)
      maxHeight(95.percent)
      width(auto)
      height(auto)
    }
  }

  val lightboxClose by style {
    background("none")
    property("border", "none")
    outline("none")
    position(Position.Absolute)
    top(0.px)
    right(0.px)
    padding(0.px, 10.px)
    fontSize(3.em)
    cursor("pointer")
    color(rgba(0, 0, 0, 0.5))
    hover {
      color(rgba(0, 0, 0, 0.8))
    }
  }
}