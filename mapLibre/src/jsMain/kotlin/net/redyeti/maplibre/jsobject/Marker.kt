@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject

import org.w3c.dom.HTMLElement

/**
 * Alignment options of rotation and pitch
 */
// type Alignment = 'map' | 'viewport' | 'auto'

/**
 * Creates a marker component
 *
 * @group Markers and Controls
 *
 * @example
 * ```ts
 * let marker = new Marker()
 *   .setLngLat([30.5, 50.5])
 *   .addTo(map)
 * ```
 *
 * @example
 * Set options
 * ```ts
 * let marker = new Marker({
 *     color: "#FFFFFF",
 *     draggable: true
 *   }).setLngLat([30.5, 50.5])
 *   .addTo(map)
 * ```
 * @see [Add custom icons with Markers](https://maplibre.org/maplibre-gl-js/docs/examples/custom-marker-icons/)
 * @see [Create a draggable Marker](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-marker/)
 *
 * ## Events
 *
 * **Event** `dragstart` of type {@link Event} will be fired when dragging starts.
 *
 * **Event** `drag` of type {@link Event} will be fired while dragging.
 *
 * **Event** `dragend` of type {@link Event} will be fired when the marker is finished being dragged.
 */
external class Marker {
  constructor()
  constructor(options: MarkerOptions)

  /**
   * Attaches the `Marker` to a `Map` object.
   * @param map - The MapLibre GL JS map to add the marker to.
   * @example
   * ```ts
   * let marker = new Marker()
   *   .setLngLat([30.5, 50.5])
   *   .addTo(map); // add the marker to the map
   * ```
   */
  fun addTo(map: Map): Marker

  /**
   * Removes the marker from a map
   * @example
   * ```ts
   * let marker = new Marker().addTo(map)
   * marker.remove()
   * ```
   */
  fun remove(): Marker

  /**
   * Get the marker's geographical location.
   *
   * The longitude of the result may differ by a multiple of 360 degrees from the longitude previously
   * set by `setLngLat` because `Marker` wraps the anchor longitude across copies of the world to keep
   * the marker on screen.
   *
   * @returns A {@link LngLat} describing the marker's location.
   * @example
   * ```ts
   * // Store the marker's longitude and latitude coordinates in a variable
   * let lngLat = marker.getLngLat()
   * // Print the marker's longitude and latitude values in the console
   * console.log('Longitude: ' + lngLat.lng + ', Latitude: ' + lngLat.lat )
   * ```
   * @see [Create a draggable Marker](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-marker/)
   */
  fun getLngLat(): LngLat

  /**
   * Set the marker's geographical position and move it.
   * @param lnglat - A {@link LngLat} describing where the marker should be located.
   * @example
   * Create a new marker, set the longitude and latitude, and add it to the map
   * ```ts
   * new Marker()
   *   .setLngLat([-65.017, -16.457])
   *   .addTo(map)
   * ```
   * @see [Add custom icons with Markers](https://maplibre.org/maplibre-gl-js/docs/examples/custom-marker-icons/)
   * @see [Create a draggable Marker](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-marker/)
   */
  fun setLngLat(lnglat: LngLat): Marker
  fun setLngLat(lnglat: Array<Double>): Marker

  /**
   * Returns the `Marker`'s HTML element.
   * @returns element
   */
  fun getElement(): HTMLElement

  /**
   * Binds a {@link Popup} to the {@link Marker}.
   * @param popup - An instance of the {@link Popup} class. If undefined or null, any popup
   * set on this {@link Marker} instance is unset.
   * @example
   * ```ts
   * let marker = new Marker()
   *  .setLngLat([0, 0])
   *  .setPopup(new Popup().setHTML("<h1>Hello World!</h1>")) // add popup
   *  .addTo(map)
   * ```
   * @see [Attach a popup to a marker instance](https://maplibre.org/maplibre-gl-js/docs/examples/set-popup/)
   */
  fun setPopup(popup : Popup): Marker

  /**
   * Set the option to allow subpixel positioning of the marker by passing a Boolean
   *
   * @param value - when set to `true`, subpixel positioning is enabled for the marker.
   *
   * @example
   * ```ts
   * let marker = new Marker()
   * marker.setSubpixelPositioning(true)
   * ```
   */
  fun setSubpixelPositioning(value: Boolean): Marker

  /**
   * Returns the {@link Popup} instance that is bound to the {@link Marker}.
   * @returns popup
   * @example
   * ```ts
   * let marker = new Marker()
   *  .setLngLat([0, 0])
   *  .setPopup(new Popup().setHTML("<h1>Hello World!</h1>"))
   *  .addTo(map)
   *
   * console.log(marker.getPopup()); // return the popup instance
   * ```
   */
  // fun getPopup(): Popup

  /**
   * Opens or closes the {@link Popup} instance that is bound to the {@link Marker}, depending on the current state of the {@link Popup}.
   * @example
   * ```ts
   * let marker = new Marker()
   *  .setLngLat([0, 0])
   *  .setPopup(new Popup().setHTML("<h1>Hello World!</h1>"))
   *  .addTo(map)
   *
   * marker.togglePopup(); // toggle popup open or closed
   * ```
   */
  fun togglePopup(): Marker

  /**
   * Get the marker's offset.
   * @returns The marker's screen coordinates in pixels.
   */
  fun getOffset(): Point

  /**
   * Sets the offset of the marker
   * @param offset - The offset in pixels as a {@link PointLike} object to apply relative to the element's center. Negatives indicate left and up.
   */
  fun setOffset(offset: Point): Marker

  /**
   * Adds a CSS class to the marker element.
   *
   * @param className - on-empty string with CSS class name to add to marker element
   *
   * @example
   * ```
   * let marker = new Marker()
   * marker.addClassName('some-class')
   * ```
   */
  fun addClassName(className: String)

  /**
   * Removes a CSS class from the marker element.
   *
   * @param className - Non-empty string with CSS class name to remove from marker element
   *
   * @example
   * ```ts
   * let marker = new Marker()
   * marker.removeClassName('some-class')
   * ```
   */
  fun removeClassName(className: String)

  /**
   * Add or remove the given CSS class on the marker element, depending on whether the element currently has that class.
   *
   * @param className - Non-empty string with CSS class name to add/remove
   *
   * @returns if the class was removed return false, if class was added, then return true
   *
   * @example
   * ```ts
   * let marker = new Marker()
   * marker.toggleClassName('toggleClass')
   * ```
   */
  fun toggleClassName(className: String): Boolean

  /**
   * Sets the `draggable` property and functionality of the marker
   * @param shouldBeDraggable - Turns drag functionality on/off
   */
  fun setDraggable(shouldBeDraggable : Boolean): Marker

  /**
   * Returns true if the marker can be dragged
   * @returns True if the marker is draggable.
   */
  fun isDraggable(): Boolean

  /**
   * Sets the `rotation` property of the marker.
   * @param rotation - The rotation angle of the marker (clockwise, in degrees), relative to its respective {@link Marker#setRotationAlignment} setting.
   */
  fun setRotation(rotation : Double): Marker

  /**
   * Returns the current rotation angle of the marker (in degrees).
   * @returns The current rotation angle of the marker.
   */
  fun getRotation(): Double

  /**
   * Sets the `rotationAlignment` property of the marker.
   * @param alignment - Sets the `rotationAlignment` property of the marker. defaults to 'auto'
   */
  fun setRotationAlignment(alignment : String): Marker   // Alignment

  /**
   * Returns the current `rotationAlignment` property of the marker.
   * @returns The current rotational alignment of the marker.
   */
  fun getRotationAlignment(): String // Alignment

  /**
   * Sets the `pitchAlignment` property of the marker.
   * @param alignment - Sets the `pitchAlignment` property of the marker. If alignment is 'auto', it will automatically match `rotationAlignment`.
   */
  fun setPitchAlignment(alignment : String): Marker // Alignment

  /**
   * Returns the current `pitchAlignment` property of the marker.
   * @returns The current pitch alignment of the marker in degrees.
   */
  fun getPitchAlignment(): String // Alignment

  /**
   * Sets the `opacity` and `opacityWhenCovered` properties of the marker.
   * When called without arguments, resets opacity and opacityWhenCovered to defaults
   * @param opacity - Sets the `opacity` property of the marker.
   * @param opacityWhenCovered - Sets the `opacityWhenCovered` property of the marker.
   */
  fun setOpacity(opacity : String, opacityWhenCovered : String): Marker
}