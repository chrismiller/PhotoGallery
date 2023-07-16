package net.redyeti.gallery.web.style

import net.redyeti.gallery.web.style.LightboxVars.CAPTION_ICON_SIZE
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
  val controlsTextColour by variable<CSSColorValue>()

  // Image
  val imageBackgroundColour by variable<CSSColorValue>()
  val imageCaptionTextColour by variable<CSSColorValue>()
  val imageCaptionSubtextColour by variable<CSSColorValue>()
  val imageCaptionLinkColour by variable<CSSColorValue>()
  val imageCaptionLinkHoverColour by variable<CSSColorValue>()

  const val CAPTION_ICON_SIZE = 20
}

@OptIn(ExperimentalComposeWebApi::class)
object LightboxStyle : StyleSheet() {
  init {
    universal style {
      LightboxVars.overlayColour(Color("#0B0B0B"))
      LightboxVars.popupPaddingLeft(8.px)

      LightboxVars.controlsColour(Color("#555555"))
      LightboxVars.controlsBorderColour(Color("#3F3F3F"))
      LightboxVars.controlsTextColour(Color("#CCCCCC"))

      LightboxVars.imageBackgroundColour(transparent)
      LightboxVars.imageCaptionTextColour(Color("#E0E0E0"))
      LightboxVars.imageCaptionSubtextColour(Color("#BBBBBB"))
      LightboxVars.imageCaptionLinkColour(Color("#E0E0E0"))
      LightboxVars.imageCaptionLinkHoverColour(Color("#FFFFFF"))
    }
  }

  val fullscreen by style {
    backgroundImage("url(\"data:image/svg+xml,%3Csvg width='${CAPTION_ICON_SIZE}px' height='${CAPTION_ICON_SIZE}px' viewBox='0 0 20 20' xmlns='http://www.w3.org/2000/svg'%3E%3Cg%3E%3Cpath fill='%23FFFFFF' d='M7 2H2v5l1.8-1.8L6.5 8 8 6.5 5.2 3.8 7 2zm6 0l1.8 1.8L12 6.5 13.5 8l2.7-2.7L18 7V2h-5zm.5 10L12 13.5l2.7 2.7L13 18h5v-5l-1.8 1.8-2.7-2.8zm-7 0l-2.7 2.7L2 13v5h5l-1.8-1.8L8 13.5 6.5 12z'/%3E%3C/g%3E%3C/svg%3E")
  }

  init {
    // :fullscreen .fullscreen { ... }
    desc(":fullscreen", className(fullscreen)) style {
      // fullscreenExit.svg after running through https://yoksel.github.io/url-encoder/
      backgroundImage("url(\"data:image/svg+xml,%3Csvg width='${CAPTION_ICON_SIZE}px' height='${CAPTION_ICON_SIZE}px' viewBox='0 0 20 20' xmlns='http://www.w3.org/2000/svg'%3E%3Cg%3E%3Cpath fill='%23FFFFFF' d='M3.4 2L2 3.4l2.8 2.8L3 8h5V3L6.2 4.8 3.4 2zm11.8 4.2L18 3.4 16.6 2l-2.8 2.8L12 3v5h5l-1.8-1.8zM4.8 13.8L2 16.6 3.4 18l2.8-2.8L8 17v-5H3l1.8 1.8zM17 12h-5v5l1.8-1.8 2.8 2.8 1.4-1.4-2.8-2.8L17 12z'/%3E%3C/g%3E%3C/svg%3E")
    }
  }

  // ----------------------------------------------------------------

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

    property("user-select", "none")

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

  init {
    "button" style {
      val self = selector("button")
      self + className(arrow) style {
        overflow("visible")
        cursor("pointer")
        backgroundColor(transparent)
        border(0.px)
        display(DisplayStyle.Block)
        outline("none")
        padding(0.px)
        property("z-index", LightboxVars.zIndexBase + 10)
        property("box-shadow", "none")
        property("touch-action", "manipulation")
      }
    }

    "img" style {
      // img.image { ... }
      selector("img") + className(image) style {
        width(auto)
        maxWidth(100.percent)
        height(auto)
        display(DisplayStyle.Block)
        lineHeight(0.px)
        boxSizing("border-box")
        padding(34.px, 0.px, 40.px)
        property("margin", "0 auto")
      }
    }
  }

  val background by style {
    fullPage()
    property("z-index", LightboxVars.zIndexBase)
    backgroundColor(LightboxVars.overlayColour.value())
    opacity(LightboxVars.overlayOpacity)
  }

  // Placeholders
  val lightboxImage by style {}
  val imageWrapper by style {}
  val lightboxCaption by style {}
  val textArea by style {}
  val captionText by style {}
  val copyrightText by style {}
  val captionCounter by style {}
  val captionIcons by style {}

  val lightbox by style {
    fullPage()
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
    alignItems(AlignItems.Center)
    justifyContent(JustifyContent.Center)
    property("z-index", LightboxVars.zIndexBase + 1)

    property("transition", "transform 1s ease")
    transitions {
      defaultDuration(1.s)
      defaultTimingFunction(AnimationTimingFunction.Ease)
    }

    className(imageWrapper) style {
      width(100.percent)
      overflow("hidden")
      display(DisplayStyle.Flex)
      alignItems(AlignItems.Center)
      justifyContent(JustifyContent.Center)
      flex(1)
    }

    // The full sized image popup
    className(lightboxImage) style {
      maxWidth(100.percent)
      maxHeight(100.percent)
      minHeight(5.percent)
      transitions {
        defaultDuration(1.s)
        defaultTimingFunction(AnimationTimingFunction.Ease)
      }
    }

    // The caption for the full sized image
    className(lightboxCaption) style {
      display(DisplayStyle.Flex)
      alignItems(AlignItems.FlexEnd)
      gap(10.px)
      width(100.percent)
      backgroundColor(Color.black)
      padding(5.px)
      className(textArea) style {
        flex(1)
        display(DisplayStyle.Flex)
        flexWrap(FlexWrap.Wrap)
        justifyContent(JustifyContent.SpaceBetween)
        paddingLeft(10.px)
        className(captionText) style {
          width(100.percent)
          textAlign("left")
          property("word-wrap", "break-word")
          fontSize(14.px)
          color(LightboxVars.imageCaptionTextColour.value())
          "a" style {
            color(LightboxVars.imageCaptionLinkColour.value())
            self + hover style {
              color(LightboxVars.imageCaptionLinkHoverColour.value())
            }
          }
        }
        group(className(copyrightText), className(captionCounter)) style {
          flex(0)
          color(LightboxVars.imageCaptionSubtextColour.value())
          marginTop(3.px)
          fontSize(12.px)
          lineHeight(14.px)
          whiteSpace("nowrap")
        }
      }
      className(captionIcons) style {
        flex(0)
        paddingRight(10.px)
        whiteSpace("nowrap")
        cursor("pointer")
        child(self, selector("div")) style {
          display(DisplayStyle.InlineBlock)
          width(CAPTION_ICON_SIZE.px)
          height(CAPTION_ICON_SIZE.px)
          marginLeft(5.px)
          opacity(0.5)
          self + hover style {
            opacity(0.8)
          }
        }
      }

      // @media "screen and..." {
      //   .LightboxStyle-bottomBar { ... }
      // }
      media("screen and (max-width: 800px) and (orientation: landscape), screen and (max-height: 400px)") {
        self style {
          // Drop the caption down to the base of the window and overlay it on the image when the screen size is small
          bottom(0.px)
          margin(0.px)
          top(auto)
          width(100.percent)
          position(Position.Fixed)
          backgroundColor(rgba(0, 0, 0, 0.5))
          boxSizing("border-box")
          self + empty style {
            padding(0.px)
          }
        }
      }
    }
  }

  private fun CSSBuilder.fullPage() {
    position(Position.Fixed)
    top(0.px)
    left(0.px)
    width(100.percent)
    height(100.vh)
    overflow("hidden")
  }
}