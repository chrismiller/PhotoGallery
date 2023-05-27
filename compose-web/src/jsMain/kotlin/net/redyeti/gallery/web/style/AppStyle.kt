package net.redyeti.gallery.web.style

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*

object StyleVars {
  val colourGreyLight by variable<CSSColorValue>()
  val colourGreyMedium by variable<CSSColorValue>()
  val colourGreyDark by variable<CSSColorValue>()

  val imageSideMargin by variable<CSSUnitValue>()
  val imageBorder by variable<CSSBorder>()

  val colCount by variable<StylePropertyNumber>()
}

object AppStyle : StyleSheet() {
  init {
    universal style {
      StyleVars.colourGreyLight(Color("#f4f4f4"))
      StyleVars.colourGreyMedium(Color("#b3b3b3"))
      StyleVars.colourGreyDark(Color("#323236"))
      StyleVars.imageSideMargin(15.px)
      margin(0.px)
    }
  }

  val pageWrapper by style {
    paddingLeft(5.percent)
    paddingRight(5.percent)
    minHeight(100.vh)
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
  }

  val albumCover by style {
    margin(10.px)
    display(DisplayStyle.InlineBlock)
    textAlign("center")
  }

  val justified by style {
    position(Position.Relative)
    backgroundColor(Color.black)
  }

  val thumb by style {
    border(1.px, LineStyle.Solid, Color.white)
    width(100.percent)
    height(100.percent)
  }

  val coverWrapper by style {
    position(Position.Relative)
    property("margin", "0 auto")
    textAlign("center")
  }

  val box by style {
    position(Position.Absolute)
    backgroundColor(StyleVars.colourGreyMedium.value())
  }

  val loading by keyframes {
    50.percent {
      property("box-shadow", "19px 0 0 3px, 38px 0 0 7px, 57px 0 0 3px")
    }
    100.percent {
      property("box-shadow", "19px 0 0 0, 38px 0 0 3px, 57px 0 0 7px")
    }
  }

  @OptIn(ExperimentalComposeWebApi::class)
  val loader by style {
    position(Position.Fixed)
    property("z-index", 9999)
    top(50.percent)
    left(50.percent)
    width(4.px)
    property("aspect-ratio", 1)
    borderRadius(50.percent)
    property("box-shadow", "19px 0 0 7px, 38px 0 0 3px, 57px 0 0 0")
    transform { translate((-50).percent, (-50).percent) }
    transform { translateX((-38).px) }
    animation(loading) {
      duration(0.8.s)
      timingFunction(AnimationTimingFunction.Linear)
      direction(AnimationDirection.Alternate)
      iterationCount(null)
    }
  }

  val headerText by style {
    property("float", "left")
    "h1" style {
      fontSize(2.em)
    }

    "h2" style {
      fontSize(1.2.em)
    }
  }

  val headerLogo by style {
    property("float", "right")
  }

  init {
    "body" style {
      fontFamily("Source Sans Pro", "sans-serif")
      color(StyleVars.colourGreyLight.value())
      backgroundColor(Color.black)
    }

    "a" style {
      color(StyleVars.colourGreyMedium.value())
      textDecoration("none")
      hover {
        color(Color.white)
      }
    }

    "header" style {
      margin(15.px, 0.px)
      backgroundColor(Color("292CFF"))
    }

    "main" style {
      // Expand the content so the page fills at least pageWrapper.minHeight
      flex(1)
    }

    "footer" style {
      fontSize(80.percent)
      backgroundColor(Color("292C2F"))
      marginTop(15.px)
      height(30.px)
      textAlign("center")
    }
  }
}