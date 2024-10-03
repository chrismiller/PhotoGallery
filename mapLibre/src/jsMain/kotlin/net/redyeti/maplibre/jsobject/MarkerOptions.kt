@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject

import kotlinx.js.JsPlainObject
import org.w3c.dom.HTMLElement

/**
 * Alignment options of rotation and pitch
 */
// type Alignment = 'map' | 'viewport' | 'auto'

/**
 * The {@link Marker} options object
 */
@JsPlainObject
external interface MarkerOptions {
  /**
   * DOM element to use as a marker. The default is a light blue, droplet-shaped SVG marker.
   */
  var element: HTMLElement?
  /**
   * Space-separated CSS class names to add to marker element.
   */
  var className: String?
  /**
   * The offset in pixels as a {@link PointLike} object to apply relative to the element's center. Negatives indicate left and up.
   */
  var offset: Point?
  /**
   * A string indicating the part of the Marker that should be positioned closest to the coordinate set via {@link Marker#setLngLat}.
   * Options are `'center'`, `'top'`, `'bottom'`, `'left'`, `'right'`, `'top-left'`, `'top-right'`, `'bottom-left'`, and `'bottom-right'`.
   * @defaultValue 'center'
   * */
  var anchor: String? // PositionAnchor
  /**
   * The color to use for the default marker if options.element is not provided. The default is light blue.
   * @defaultValue '#3FB1CE'
   */
  var color: String?
  /**
   * The scale to use for the default marker if options.element is not provided. The default scale corresponds to a height of `41px` and a width of `27px`.
   * @defaultValue 1
   */
  var scale: Double?
  /**
   * A Boolean indicating whether or not a marker is able to be dragged to a new position on the map.
   * @defaultValue false
   */
  var draggable: Boolean?
  /**
   * The max Double of pixels a user can shift the mouse pointer during a click on the marker for it to be considered a valid click (as opposed to a marker drag). The default is to inherit map's clickTolerance.
   * @defaultValue 0
   */
  var clickTolerance: Double?
  /**
   * The rotation angle of the marker in degrees, relative to its respective `rotationAlignment` setting. A positive value will rotate the marker clockwise.
   * @defaultValue 0
   */
  var rotation: Double?
  /**
   * `map` aligns the `Marker`'s rotation relative to the map, maintaining a bearing as the map rotates. `viewport` aligns the `Marker`'s rotation relative to the viewport, agnostic to map rotations. `auto` is equivalent to `viewport`.
   * @defaultValue 'auto'
   */
  var rotationAlignment: String? // Alignment
  /**
   * `map` aligns the `Marker` to the plane of the map. `viewport` aligns the `Marker` to the plane of the viewport. `auto` automatically matches the value of `rotationAlignment`.
   * @defaultValue 'auto'
   */
  var pitchAlignment: String? // Alignment
  /**
   * Marker's opacity when it's in clear view (not behind 3d terrain)
   * @defaultValue 1
   */
  var opacity: String?
  /**
   * Marker's opacity when it's behind 3d terrain
   * @defaultValue 0.2
   */
  var opacityWhenCovered: String?
  /**
   * If `true`, rounding is disabled for placement of the marker, allowing for
   * subpixel positioning and smoother movement when the marker is translated.
   * @defaultValue false
   */
  var subpixelPositioning: Boolean?
}
