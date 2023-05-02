package net.redyeti.gallery.web.style

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.Color.transparent
import org.jetbrains.compose.web.css.keywords.auto

object LightboxVars {
  const val zIndexBase = 2000

  // Overlay
  const val overlayOpacity = 0.7
  val overlayColour by variable<CSSColorValue>()

  // Spacing
  val popupPaddingLeft by variable<CSSUnitValue>()

  // Controls
  const val controlsOpacity = 0.7
  val controlsColour by variable<CSSColorValue>()
  val controlsBorderColour by variable<CSSColorValue>()
  val controlsCloseColour by variable<CSSColorValue>()
  val controlsTextColour by variable<CSSColorValue>()
  val controlsTextHoverColour by variable<CSSColorValue>()

  // Image
  val imageBackgroundColour by variable<CSSColorValue>()
  val imageCaptionTitleColour by variable<CSSColorValue>()
  val imageCaptionSubtitleColour by variable<CSSColorValue>()
  val imagePaddingTop by variable<CSSLengthOrPercentageValue>()
  val imagePaddingBottom by variable<CSSUnitValue>()
  val bottomBarMargin by variable<CSSUnitValue>()
}

@OptIn(ExperimentalComposeWebApi::class)
object LightboxStyle : StyleSheet() {
  init {
    universal style {
      LightboxVars.overlayColour(Color("#0B0B0B"))
      LightboxVars.popupPaddingLeft(8.px)

      LightboxVars.controlsColour(Color("#202020"))
      LightboxVars.controlsBorderColour(Color("#3F3F3F"))
      LightboxVars.controlsCloseColour(Color("#333333"))
      LightboxVars.controlsTextColour(Color("#CCCCCC"))
      LightboxVars.controlsTextHoverColour(Color("#FFFFFF"))

      LightboxVars.imageBackgroundColour(Color("#444444"))
      LightboxVars.imageCaptionTitleColour(Color("#F3F3F3"))
      LightboxVars.imageCaptionSubtitleColour(Color("#BBBBBB"))
      LightboxVars.imagePaddingTop(40.px)
      LightboxVars.imagePaddingBottom(40.px)
      LightboxVars.bottomBarMargin(4.px - LightboxVars.imagePaddingBottom.value())
    }
  }

  val background by style {
    top(0.px)
    left(0.px)
    width(100.percent)
    height(100.percent)
    property("z-index", LightboxVars.zIndexBase + 2)
    overflow("hidden")
    position(Position.Fixed)
    backgroundColor(LightboxVars.overlayColour.value())
    opacity(LightboxVars.overlayOpacity)
  }

  val popup by style {
    top(0.px)
    left(0.px)
    width(100.percent)
    height(100.percent)
    property("z-index", LightboxVars.zIndexBase + 3)
    position(Position.Fixed)
    outline("none !important")
  }

  val container by style {
    textAlign("center")
    position(Position.Absolute)
    width(100.percent)
    height(100.percent)
    top(0.px)
    left(0.px)
    paddingTop(0.px)
    paddingBottom(0.px)
    paddingLeft(LightboxVars.popupPaddingLeft.value())
    paddingRight(LightboxVars.popupPaddingLeft.value())
    boxSizing("border-box")

    // .LightboxStyle-lbContainer::before { ... }
    self + before style {
      property("content", "''")
      display(DisplayStyle.InlineBlock)
      height(100.percent)
      property("vertical-align", "middle")
    }
  }

  val alignTop by style {
    // .LightboxStyle-lbAlignTop .LightboxStyle-lbContainer::before { ... }
    className(container) + before style {
      display(DisplayStyle.None)
    }
  }

  val content by style {
    position(Position.Relative)
    display(DisplayStyle.InlineBlock)
    property("vertical-align", "middle")
    property("margin", "0 auto")
    textAlign("left")
    property("z-index", LightboxVars.zIndexBase + 5)
  }

  val inlineHolder by style {
    className(content) style {
      width(100.percent)
      cursor("auto")
    }
  }

  val autoCursor by style {
    className(content) style {
      cursor("auto")
    }
  }

  val hide by style {
    property("display", "none !important")
  }

  val preloader by style {
    color(Color("#CCCCCC"))
    position(Position.Absolute)
    top(50.percent)
    width(auto)
    textAlign("center")
    marginTop(-0.8.em)
    left(8.px)
    right(8.px)
    property("z-index", LightboxVars.zIndexBase + 4)
    type("a") style {
      color(Color("#CCC"))
      self + hover style {
        color(Color("#FFF"))
      }
    }
  }

  val close by style {
    width(44.px)
    height(44.px)
    lineHeight(44.px)
    position(Position.Absolute)
    top(0.px)
    right(0.px)
    textDecoration("none")
    textAlign("center")
    opacity(LightboxVars.controlsOpacity)
    padding(0.px, 0.px, 18.px, 10.px)
    color(LightboxVars.controlsColour.value())
    fontStyle("normal")
    fontSize(28.px)
    fontFamily("Arial")
    cursor("zoom-out")
    group(self + hover, self + focus) style {
      opacity(1)
    }
    self + active style {
      top(1.px)
    }
  }

  val closeButtonIn by style {
    className(close) style {
      color(LightboxVars.controlsCloseColour.value())
    }
  }

  val imageHolder by style {
    className(close) style {
      color(LightboxVars.controlsColour.value())
      right((-6).px)
      textAlign("right")
      paddingRight(6.px)
      width(100.percent)
    }
    className(content) style {
      maxWidth(100.percent)
    }
  }

  val counter by style {
    position(Position.Absolute)
    top(0.px)
    right(0.px)
    color(LightboxVars.controlsTextColour.value())
    fontSize(12.px)
    lineHeight(18.px)
    whiteSpace("nowrap")
  }

  val arrow by style {
    position(Position.Absolute)
    opacity(LightboxVars.controlsOpacity)
    margin(0.px)
    top(50.percent)
    marginTop((-55).px)
    padding(0.px)
    width(90.px)
    height(110.px)
    active {
      marginTop((-54).px)
    }
    group(self + hover, self + focus) style {
      opacity(1)
    }

    group(self + before, self + after) style {
      property("content", "''")
      display(DisplayStyle.Block)
      width(0.px)
      height(0.px)
      position(Position.Absolute)
      left(0.px)
      top(0.px)
      marginLeft(35.px)
      marginTop(35.px)
      property("border", "medium inset transparent")
    }
    self + before style {
      property("border-top-width", 21.px)
      property("border-bottom-width", 21.px)
      opacity(0.7)
    }

    self + after style {
      property("border-top-width", 13.px)
      property("border-bottom-width", 13.px)
      top(8.px)
    }

    // .LightboxStyle-lbPreloader, .LightboxStyle-lbArrow, .LightboxStyle-lbClose, .LightboxStyle-lbCounter { ... }
    group(self, className(preloader), className(close), className(counter)) style {
      property("user-select", "none")
    }

    media(mediaMaxWidth(900.px)) {
      self style {
        transform {
          scale(0.75)
        }
      }
    }
  }

  val arrowLeft by style {
    left(0.px)
    self + before style {
      property("border-right", "27px solid ${LightboxVars.controlsBorderColour.value()}")
      marginLeft(25.px)
    }
    self + after style {
      property("border-right", "17px solid ${LightboxVars.controlsBorderColour.value()}")
      marginLeft(31.px)
    }
    media(mediaMaxWidth(900.px)) {
      self style {
        property("transform-origin", "0")
      }
    }
  }

  val arrowRight by style {
    right(0.px)
    self + before style {
      property("border-left", "27px solid ${LightboxVars.controlsBorderColour.value()}")
    }
    self + after style {
      property("border-left", "17px solid ${LightboxVars.controlsBorderColour.value()}")
      marginLeft(39.px)
    }
    media(mediaMaxWidth(900.px)) {
      self style {
        property("transform-origin", "100%")
      }
    }
  }

  val image by style {
  }

  val figure by style {
    lineHeight(0.px)
    self + after style {
      property("content", "''")
      position(Position.Absolute)
      display(DisplayStyle.Block)
      left(0.px)
      right(0.px)
      // TODO
      // top(LightboxVars.imagePaddingTop.value())
      // bottom(LightboxVars.imagePaddingTop.value())
      top(40.px)
      bottom(40.px)
      width(auto)
      height(auto)
      property("z-index", "-1")
      property("box-shadow", "0 0 8px rgba(0, 0, 0, 0.6)")
      backgroundColor(LightboxVars.imageBackgroundColour.value())
    }
    "small" style {
      color(LightboxVars.imageCaptionSubtitleColour.value())
      display(DisplayStyle.Block)
      fontSize(12.px)
      lineHeight(14.px)
    }
    "figure" style {
      margin(0.px)
    }
  }

  val bottomBar by style {
    marginTop(LightboxVars.bottomBarMargin.value())
    position(Position.Absolute)
    top(100.percent)
    left(0.px)
    width(100.percent)
    cursor("auto")

    // @media "screen and..." {
    //   .LightboxStyle-bottomBar { ... }
    // }
    media("screen and (max-width: 800px) and (orientation: landscape), screen and (max-height: 400px)") {
      self style {
        // Drop the bottom bar down to the base of the window when the screen size is small
        bottom(0.px)
        margin(0.px)
        top(auto)
        padding(3.px, 5.px)
        position(Position.Fixed)
        boxSizing("border-box")
        self + empty style {
          padding(0.px)
        }
      }
    }

  }

  val title by style {
    textAlign("left")
    lineHeight(18.px)
    color(LightboxVars.imageCaptionTitleColour.value())
    property("word-wrap", "break-word")
    paddingRight(50.px)
  }

  val loading by style {
    self + className(figure) style {
      display(DisplayStyle.None)
    }
  }

  val ready by style {
    className(preloader) style {
      display(DisplayStyle.None)
    }
  }

  val error by style {
    className(content) style {
      display(DisplayStyle.None)
    }
  }

  init {
    "button" style {
      val self = selector("button")
      group(self + className(close), self + className(arrow)) style {
        overflow("visible")
        cursor("pointer")
        backgroundColor(transparent)
        border(0.px)
        display(DisplayStyle.Block)
        outline("none")
        padding(0.px)
        property("z-index", LightboxVars.zIndexBase + 6)
        property("box-shadow", "none")
        property("touch-action", "manipulation")
      }
    }

    "img" style {
      selector("img") + className(image) style {
        width(auto)
        maxWidth(100.percent)
        height(auto)
        display(DisplayStyle.Block)
        lineHeight(0.px)
        boxSizing("border-box")
        padding(40.px, 0.px, 40.px)
        property("margin", "0 auto")
      }
    }
  }
}