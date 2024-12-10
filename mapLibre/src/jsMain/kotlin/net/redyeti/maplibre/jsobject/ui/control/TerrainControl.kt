@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject.ui.control

import net.redyeti.maplibre.jsobject.IControl
import net.redyeti.maplibre.jsobject.Map
import org.w3c.dom.HTMLElement

/**
 * A `TerrainControl` control contains a button for turning the terrain on and off.
 *
 * @group Markers and Controls
 *
 * @example
 * ```ts
 * let map = new Map({TerrainControl: false})
 *     .addControl(new TerrainControl({
 *         source: "terrain"
 *     }));
 * ```
 */
external class TerrainControl: IControl {
  constructor()

  override fun onAdd(map: Map): HTMLElement

  override fun onRemove(map: Map)

  override val getDefaultPosition: (() -> String)?
}
