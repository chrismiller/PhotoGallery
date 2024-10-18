package net.redyeti.maplibre.jsobject.source

import net.redyeti.maplibre.jsobject.Map
import net.redyeti.maplibre.jsobject.stylespec.CanvasSourceSpecification
import org.w3c.dom.HTMLCanvasElement

/**
 * A data source containing the contents of an HTML canvas. See {@link CanvasSourceSpecification} for detailed documentation of options.
 *
 * @group Sources
 *
 * @example
 * ```ts
 * // add to map
 * map.addSource('some id', {
 *    type: 'canvas',
 *    canvas: 'idOfMyHTMLCanvas',
 *    animate: true,
 *    coordinates: [
 *        [-76.54, 39.18],
 *        [-76.52, 39.18],
 *        [-76.52, 39.17],
 *        [-76.54, 39.17]
 *    ]
 * })
 *
 * // update
 * let mySource = map.getSource('some id')
 * mySource.setCoordinates([
 *     [-76.54335737228394, 39.18579907229748],
 *     [-76.52803659439087, 39.1838364847587],
 *     [-76.5295386314392, 39.17683392507606],
 *     [-76.54520273208618, 39.17876344106642]
 * ])
 *
 * map.removeSource('some id');  // remove
 * ```
 */
external class CanvasSource: ImageSource {
  //var options: CanvasSourceSpecification
  var animate: Boolean
  var canvas: HTMLCanvasElement
  var width: Double
  var height: Double
  /**
   * Enables animation. The image will be copied from the canvas to the map on each frame.
   */
  //play: () => void
  /**
   * Disables animation. The map will display a static copy of the canvas image.
   */
  //pause: () => void

  /** @internal */
  //constructor(id: String, options: CanvasSourceSpecification, dispatcher: Dispatcher, eventedParent: Evented)

  /**
   * Returns the HTML `canvas` element.
   *
   * @returns The HTML `canvas` element.
   */
  fun getCanvas(): HTMLCanvasElement

  override fun onAdd(map: Map)

  override fun prepare()

  override fun serialize(): CanvasSourceSpecification

  override fun hasTransition(): Boolean
}