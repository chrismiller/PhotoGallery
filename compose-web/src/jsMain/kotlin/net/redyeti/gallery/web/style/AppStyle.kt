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
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
    margin(10.px)
    display(DisplayStyle.InlineBlock)
    minWidth(0.px)
    minHeight(0.px)
  }

  val photoGrid by style {
    position(Position.Relative)
    backgroundColor(Color.black)
  }

  val thumbnailContainer by style {
    position(Position.Relative)
    width(100.percent)
    height(100.percent)
  }

  @OptIn(ExperimentalComposeWebApi::class)
  val interactionView by style {
    position(Position.Absolute)
    left(0.px)
    top(0.px)
    width(100.percent)
    height(100.percent)
    opacity(0.0)
    transitions {
      "opacity" {
        duration(0.3.s)
        timingFunction(AnimationTimingFunction.EaseInOut)
      }
    }
    self + hover style {
      opacity(1.0)
      backgroundImage("linear-gradient(transparent 65%, rgba(0,0,0,0.35))")
    }
  }

  val truncatedText by style {
    display(DisplayStyle.Block)
    flex(1)
    overflow("hidden")
    property("text-overflow", "ellipsis")
    property("text-shadow", "0 0 3px #000")
    whiteSpace("nowrap")
    width(100.percent)
  }

  val thumbText by style {
    flex(1)
    minWidth(0.px)
    minHeight(0.px)
    marginRight(8.px)
  }

  val thumbTitle by style {
    fontSize(14.px)
    color(Color.white)
  }

  val thumbSubTitle by style {
    fontSize(11.px)
    color(Color.white)
  }

  val photoInteraction by style {
    cursor("pointer")
    width(100.percent)
    height(100.percent)
    position(Position.Absolute)
  }

  val interactionBar by style {
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Row)
    alignItems(AlignItems.FlexEnd)
    bottom(0.px)
    left(0.px)
    padding(0.px, 4.px, 4.px)
    boxSizing("border-box")
    position(Position.Absolute)
    width(100.percent)
    property("z-index", "2")
  }

  val interactionItem by style {
    cursor("pointer")
    display(DisplayStyle.Flex)
    minWidth(0.px)
    minHeight(0.px)
    marginRight(4.px)
    color(Color.white)
    self + firstChild style {
      flex(1)
    }
    self + lastChild style {
      marginRight(0.px)
    }
  }

  val thumb by style {
    border(1.px, LineStyle.Solid, Color.white)
    width(100.percent)
    height(100.percent)
  }

  val coverWrapper by style {
    position(Position.Relative)
    textAlign("center")
    property("margin", "0 auto")
  }

  val photoGridCell by style {
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

  val sideMenu by style {
    position(Position.Fixed)
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
    minWidth(0.px)
    minHeight(0.px)
    margin(24.px)
    gap(12.px)
    property("z-index", 100)
  }

  @OptIn(ExperimentalComposeWebApi::class)
  val sideImageSelected by style {
    filter { saturate(0.percent) }
  }

  @OptIn(ExperimentalComposeWebApi::class)
  val sideImg by style {
    filter { saturate(50.percent) }
    width(40.px)
    height(40.px)
    self + hover style {
      filter { saturate(100.percent) }
    }
  }

  val headerText by style {
    property("float", "left")
    "h1" style {
      fontSize(2.em)
    }

    "h2" style {
      fontSize(1.1.em)
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
      color(StyleVars.colourGreyMedium.value())
      backgroundColor(Color("292C2F"))
      marginTop(15.px)
      height(30.px)
      textAlign("center")
    }
  }
}