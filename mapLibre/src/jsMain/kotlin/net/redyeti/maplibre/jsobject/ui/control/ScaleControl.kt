@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject.ui.control

import kotlinx.js.JsPlainObject
import net.redyeti.maplibre.jsobject.IControl
import net.redyeti.maplibre.jsobject.Map
import org.w3c.dom.HTMLElement
import seskar.js.JsValue

/**
 * The unit type for length to use for the {@link ScaleControl}
 */
sealed external interface ScaleUnit {
  companion object {
    @JsValue("imperial")
    val Imperial: ScaleUnit
    @JsValue("metric")
    val Metric: ScaleUnit
    @JsValue("nautical")
    val Nautical: ScaleUnit
  }
}

/**
 * The {@link ScaleControl} options object
 */
@JsPlainObject
external interface ScaleControlOptions {
  /**
   * The maximum length of the scale control in pixels.
   * @defaultValue 100
   */
  var maxWidth: Double?

  /**
   * Unit of the distance (`'imperial'`, `'metric'` or `'nautical'`).
   * @defaultValue 'metric'
   */
  var unit: ScaleUnit?
}

/**
 * A `ScaleControl` control displays the ratio of a distance on the map to the corresponding distance on the ground.
 *
 * @group Markers and Controls
 *
 * @example
 * ```ts
 * let scale = new ScaleControl({
 *     maxWidth: 80,
 *     unit: 'imperial'
 * });
 * map.addControl(scale);
 *
 * scale.setUnit('metric');
 * ```
 */
external class ScaleControl: IControl {
  constructor()
  /**
   * @param options - the control's options
   */
  constructor(options: ScaleControlOptions?)

  override fun onAdd(map: Map): HTMLElement

  override fun onRemove(map: Map)

  override val getDefaultPosition: (() -> String)?

  /**
   * Set the scale's unit of the distance
   *
   * @param unit - Unit of the distance (`'imperial'`, `'metric'` or `'nautical'`).
   */
  fun setUnit(unit: ScaleUnit)
}