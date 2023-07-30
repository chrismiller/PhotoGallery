package net.redyeti.gallery.web.style

import net.redyeti.gallery.web.style.LightboxVars.CAPTION_ICON_SIZE
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto

object LightboxVars {
  const val zIndexBase = 2000

  // Overlay
  const val overlayOpacity = 0.7
  val overlayColour by variable<CSSColorValue>()

  // Spacing
  val popupPaddingLeft by variable<CSSUnitValue>()

  // Controls
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

      LightboxVars.imageBackgroundColour(Color.transparent)
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
    color(Color.white)
    position(Position.Absolute)
    width(52.px)
    height(52.px)
    lineHeight(52.px)
    textAlign("center")
    borderRadius(50.percent)  // This makes a circle
    backgroundColor(rgba(16, 16, 16, 0.5))
    top(50.percent)
    transform { translateY((-50).percent) }
    property("z-index", LightboxVars.zIndexBase + 10)
    cursor("pointer")

    opacity(0)
    // property("pointer-events", "none")
    transitions {
      "opacity" {
        duration(0.3.s)
        timingFunction(AnimationTimingFunction.Ease)
      }
      "color" {
        duration(0.1.s)
        timingFunction(AnimationTimingFunction.EaseIn)
      }
    }

    // .arrow::before
    self + before style {
      backgroundColor(Color.currentColor)
      property("content", "\"\"")
      display(DisplayStyle.InlineBlock)
      width(35.px)
      height(35.px)
      property("-webkit-mask-size", "cover")
      property("mask-size", "cover")
      property("vertical-align", "middle")
    }

    // .arrow::hover
    self + hover style {
      color(rgb(200, 200, 200))
    }
  }

  val arrowPrev by style {
    left(24.px)
    self + before style {
      property("-webkit-mask-image", "url(/left.svg)")
      property("mask-image", "url(/left.svg)")
    }
  }

  val arrowNext by style {
    right(24.px)
    self + before style {
      property("-webkit-mask-image", "url(/right.svg)")
      property("mask-image", "url(/right.svg)")
    }
  }

  val image by style {
  }

  init {
    "button" style {
      val self = type("button")
      self + className(arrow) style {
        overflow("visible")
        cursor("pointer")
        backgroundColor(Color.transparent)
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
      type("img") + className(image) style {
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

    group(type("figure"), type("section"), type("header")) style {
      margin(0.px)
      padding(0.px)
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

        // TODO: Fix this?
        // .lightbox .lightboxCaption .textArea .copyrightText, .captionCounter
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

        // .lightbox .lightboxCaption .captionIcons > div
        child(self, type("div")) style {
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

  val showInfoPanel by style {}
  val infoPanelHeader by style {}
  val infoSection by style {}
  val infoItem by style {}
  val detailItem by style {}
  val closeInfoPanel by style {}
  val cameraInfo by style {}
  val dateInfo by style {}
  val fileInfo by style {}
  val locationInfo by style {}
  val label by style {}
  val subs by style {}

  // Lays out the info panel
  val infoPanel by style {
    position(Position.Fixed)
    right((-350).px)
    height(100.percent)
    left(auto)
    top(0.px)
    width(350.px)
    textAlign("center")
    backgroundColor(Color.white)
    overflowY("auto")
    property("z-index", LightboxVars.zIndexBase + 1)  // Lift it above the shaded background
    property("transition", "right 0.3s ease")

    "h4" style {
      marginBottom(10.px)
    }

    "a" style {
      color(rgb(16, 130, 150))
    }

    // .infoPanel .label, .infoPanel .subs { ... }
    group(desc(self, className(label)), desc(self, className(subs))) style {
      display(DisplayStyle.Block)
      fontSize(14.px)
    }

    className(label) style {
      whiteSpace("nowrap")
      lineHeight(18.px)
    }

    className(subs) style {
      property("word-wrap", "break-word")
      lineHeight(17.px)
      marginTop(4.px)
      "span" style {
        color(rgb(120, 120, 120))
        marginTop(10.px)
        marginRight(15.px)
      }
    }

    // Positions the info panel header
    className(infoPanelHeader) style {
      textAlign("center")
      padding(25.px, 25.px, 0.px)

      // Positions the 'close' button for the info panel
      className(closeInfoPanel) style {
        self + before style {
          backgroundColor(Color.currentColor)
          property("content", "\"\"")
          display(DisplayStyle.InlineBlock)
          height(20.px)
          width(20.px)
          property("-webkit-mask-image", "url(/cancel.svg)")
          property("-mask-image", "url(/cancel.svg)")
          property("-webkit-mask-size", "cover")
          property("-mask-size", "cover")
          property("vertical-align", "middle")
        }

        // Moves the 'close' button for the info panel to the RHS
        property("float", "right")
        marginTop(2.px)
        lineHeight("normal")
        property("z-index", LightboxVars.zIndexBase + 5)
      }
    }

    // Set the text style for the info panel
    // .infoPanel h2, .infoPanel h4, .infoPanel p, .infoPanel span
    group(desc(self, "h2"), desc(self, "h4"), desc(self, "p"), desc(self, "span")) style {
      color(rgb(16, 16, 16))
    }

    className(infoSection) style {
      className(infoItem) style {
        margin(0.px, 25.px)
        padding(24.px, 0.px, 20.px)
        property("border-bottom", "1px solid #F1F1F1")
        minHeight(65.px)
        textAlign("left")

        // .infoPanel .infoSection .infoItem div.cameraInfo::before
        "div" + className(cameraInfo) + before style {
          property("-webkit-mask-image", "url(/cameraInfo.svg)")
          property("mask-image", "url(/cameraInfo.svg)")
        }
        "div" + className(dateInfo) + before style {
          property("-webkit-mask-image", "url(/dateInfo.svg)")
          property("mask-image", "url(/dateInfo.svg)")
        }
        "div" + className(fileInfo) + before style {
          property("-webkit-mask-image", "url(/fileInfo.svg)")
          property("mask-image", "url(/fileInfo.svg)")
        }
        "div" + className(locationInfo) + before style {
          property("-webkit-mask-image", "url(/location.svg)")
          property("mask-image", "url(/location.svg)")
        }

        // .infoPanel .infoSection .infoItem div.detailItem::before
        "div" + className(detailItem) + before style {
          backgroundColor(Color.currentColor)
          property("content", "\"\"")
          display(DisplayStyle.InlineBlock)
          height(22.px)
          width(22.px)
          property("-webkit-mask-size", "cover")
          property("mask-size", "cover")
          property("vertical-align", "middle")
          position(Position.Absolute)
          top(0.px)
          left(0.px)
          color(rgb(16, 16, 16))
        }

        // .infoPanel .infoSection .infoItem div.detailItem
        "div" + className(detailItem) style {
          display(DisplayStyle.Block)
          textAlign("left")
          position(Position.Relative)
          paddingLeft(40.px)
          lineHeight(20.px)
        }
      }
    }
  }

  val back by style {}
  val options by style {}
  val infoOption by style {}
  val locationOption by style {}

  val galleryHeader by style {
    // Style the 'Back' link shown at the top left of an image
    // .galleryHeader .back
    className(back) style {
      color(Color.white)
      property("transition", "color 100ms ease-in")
      property("text-shadow", "0 1px 5px rgba(16, 16, 16, 0.5)")
      fontSize(16.px)
      lineHeight(20.px)
      whiteSpace("nowrap")
      textAlign("left")
      display(DisplayStyle.Flex)
      alignItems(AlignItems.Center)
      cursor("pointer")
      "span" style {
        marginLeft(12.px)
        height(22.px)
        lineHeight(22.px)
        display(DisplayStyle.InlineBlock)
      }

      // .galleryHeader .back::before
      self + before style {
        backgroundColor(Color.currentColor)
        property("content", "\"\"")
        display(DisplayStyle.InlineBlock)
        width(20.px)
        height(20.px)
        property("-webkit-mask-image", "url(/back.svg)")
        property("mask-image", "url(/back.svg)")
        property("-webkit-mask-size", "cover")
        property("mask-size", "cover")
        property("vertical-align", "middle")
      }

      // .galleryHeader .back::hover
      self + hover style {
        color(rgb(200, 200, 200))
      }
    }

    className(options) style {
      display(DisplayStyle.Inline)
      flexGrow(1)
      textAlign("right")

      type("button") style {
        color(Color.white)
        property("transition", "color 100ms ease-in")
        property("text-shadow", "0 1px 5px rgba(16, 16, 16, 0.5)")

        type("span") style {
          marginLeft(8.px)
          display(DisplayStyle.None)
        }

        self + before style {
          backgroundColor(Color.currentColor)
          property("content", "\"\"")
          display(DisplayStyle.InlineBlock)
          width(22.px)
          height(22.px)
          property("-webkit-mask-size", "cover")
          property("mask-size", "cover")
          property("vertical-align", "middle")
        }

        self + hover style {
          color(rgb(200, 200, 200))
        }
      }

      type("button") + className(infoOption) style {
        self + before style {
          property("-webkit-mask-image", "url(/info.svg)")
          property("mask-image", "url(/info.svg)")
        }
      }

      type("button") + className(locationOption) style {
        self + before style {
          property("-webkit-mask-image", "url(/location.svg)")
          property("mask-image", "url(/location.svg)")
        }
      }

      // .galleryHeader .options button, .infoPanel button
      group(desc(self, type("button")), className(infoPanel)) style {
        type("button") style {
          property("border", "none")
          background("none")
          margin(0.px)
          padding(0.px)
          font("inherit")
          cursor("pointer")
          textAlign("inherit")
        }
      }

      type("li") style {
        display(DisplayStyle.InlineBlock)
        marginLeft(24.px)
        fontSize(15.px)
        lineHeight(18.px)
        color(rgb(16, 16, 16))
      }
      group(desc(self, type("ul")), desc(self, type("li"))) style {
        padding(0.px)
        listStyle("none")
      }
    }
  }

  val showControls by style {}

  val gallery by style {
    // Makes the prev/next and top controls visible
    self + className(showControls) style {
      // .gallery.showControls .arrow
      className(arrow) style {
        opacity(1)
        // property("pointer-events", "all")
      }
      // .gallery.showControls .galleryHeader
      className(galleryHeader) style {
        opacity(1)
        // property("pointer-events", "all")
      }
    }

    // Animate when the lightbox appears/disappears
    className(infoPanel) style {
      property("transition", "right 250ms ease, bottom 250ms ease")
    }

    className(galleryHeader) style {
      opacity(0)
      // property("pointer-events", "none")
      width(100.percent)
      maxWidth(100.percent)
      display(DisplayStyle.Flex)
      alignItems("center")
      position(Position.Fixed)
      top(0.px)
      left(0.px)
      height(88.px)
      background("linear-gradient(180deg, rgba(16, 16, 16, 0.75), rgba(16, 16, 16, 0))")
      backgroundColor(Color.transparent)
      property("margin", "0 auto")
      padding(0.px, 24.px)
      boxSizing("border-box")
      property("transition", "opacity 250ms ease, width 250ms ease")
      property("z-index", LightboxVars.zIndexBase + 5)  // Lift the header above the image
    }

    // .gallery.showInfoPanel
    self + className(showInfoPanel) style {
      // .gallery.showInfoPanel .infoPanel
      className(infoPanel) style {
        // Move the info panel so it is visible on screen
        right(0.px)
        property("box-shadow", "0 0 10px 1px rgba(0,0,0,.1)")
      }
      // Moves the image left to make room for the info panel when it is visible
      className(lightbox) style {
        right(350.px)
        bottom(0.px)
      }
      className(galleryHeader) style {
        // Reduce the size of the control area when the info panel is visible
        property("width", "calc(100% - 350px)")
      }
    }
  }

  private fun CSSBuilder.fullPage() {
    position(Position.Fixed)
    top(0.px)
    left(0.px)
    right(0.px)
    height(100.vh)
    overflow("hidden")
  }
}