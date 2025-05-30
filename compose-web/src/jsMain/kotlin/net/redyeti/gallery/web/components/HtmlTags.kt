package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

private class ElementBuilderImplementation<TElement : Element>(private val tagName: String) : ElementBuilder<TElement> {
  private val el: Element by lazy { document.createElement(tagName) }
  @Suppress("UNCHECKED_CAST")
  override fun create(): TElement = el.cloneNode() as TElement
}
private val Figure: ElementBuilder<HTMLElement> = ElementBuilderImplementation("figure")
private val FigCaption: ElementBuilder<HTMLElement> = ElementBuilderImplementation("figcaption")

@Composable
fun Figure(
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ContentBuilder<HTMLElement>? = null
) = TagElement(elementBuilder = Figure, applyAttrs = attrs, content = content)

@Composable
fun FigCaption(
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ContentBuilder<HTMLElement>? = null
) = TagElement(elementBuilder = FigCaption, applyAttrs = attrs, content = content)
