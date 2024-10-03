@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject

import org.w3c.dom.HTMLElement

external interface NavigationControlOptions {
  /**
   * If `true` the compass button is included.
   */
  var showCompass: Boolean?
  /**
   * If `true` the zoom-in and zoom-out buttons are included.
   */
  var showZoom: Boolean?
  /**
   * If `true` the pitch is visualized by rotating X-axis of compass.
   */
  var visualizePitch: Boolean?
}

external class NavigationControl: IControl {
  constructor()
  /**
   * @param options - the control's options
   */
  constructor(options: NavigationControlOptions?)

  override fun onAdd(map: Map): HTMLElement

  override fun onRemove(map: Map)

  override val getDefaultPosition: (() -> String)?
}
