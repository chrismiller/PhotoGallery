@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject.ui.control

import kotlinx.js.JsPlainObject
import net.redyeti.maplibre.jsobject.Evented
import net.redyeti.maplibre.jsobject.FitBoundsOptions
import net.redyeti.maplibre.jsobject.IControl
import net.redyeti.maplibre.jsobject.Map
import org.w3c.dom.HTMLElement

/**
 * The {@link GeolocateControl} options object
 */
@JsPlainObject
external interface GeolocateControlOptions {
  /**
   * A Geolocation API [PositionOptions](https://developer.mozilla.org/en-US/docs/Web/API/PositionOptions) object.
   * @defaultValue `{enableHighAccuracy: false, timeout: 6000}`
   */
  //var positionOptions: PositionOptions?

  /**
   * A options object to use when the map is panned and zoomed to the user's location. The default is to use a `maxZoom` of 15 to limit how far the map will zoom in for very accurate locations.
   */
  var fitBoundsOptions: FitBoundsOptions?

  /**
   * If `true` the `GeolocateControl` becomes a toggle button and when active the map will receive updates to the user's location as it changes.
   * @defaultValue false
   */
  var trackUserLocation: Boolean?
  /**
   * By default, if `showUserLocation` is `true`, a transparent circle will be drawn around the user location indicating the accuracy (95% confidence level) of the user's location. Set to `false` to disable. Always disabled when `showUserLocation` is `false`.
   * @defaultValue true
   */
  var showAccuracyCircle: Boolean?
  /**
   * By default a dot will be shown on the map at the user's location. Set to `false` to disable.
   * @defaultValue true
   */
  var showUserLocation: Boolean?
}

/**
 * A `GeolocateControl` control provides a button that uses the browser's geolocation
 * API to locate the user on the map.
 *
 * Not all browsers support geolocation,
 * and some users may disable the feature. Geolocation support for modern
 * browsers including Chrome requires sites to be served over HTTPS. If
 * geolocation support is not available, the `GeolocateControl` will show
 * as disabled.
 *
 * The zoom level applied will depend on the accuracy of the geolocation provided by the device.
 *
 * The `GeolocateControl` has two modes. If `trackUserLocation` is `false` (default) the control acts as a button, which when pressed will set the map's camera to target the user location. If the user moves, the map won't update. This is most suited for the desktop. If `trackUserLocation` is `true` the control acts as a toggle button that when active the user's location is actively monitored for changes. In this mode the `GeolocateControl` has three interaction states:
 * * active - the map's camera automatically updates as the user's location changes, keeping the location dot in the center. Initial state and upon clicking the `GeolocateControl` button.
 * * passive - the user's location dot automatically updates, but the map's camera does not. Occurs upon the user initiating a map movement.
 * * disabled - occurs if Geolocation is not available, disabled or denied.
 *
 * These interaction states can't be controlled programmatically, rather they are set based on user interactions.
 *
 * ## State Diagram
 * ![GeolocateControl state diagram](https://github.com/maplibre/maplibre-gl-js/assets/3269297/78e720e5-d781-4da8-9803-a7a0e6aaaa9f)
 *
 * @group Markers and Controls
 *
 * @example
 * ```ts
 * map.addControl(new GeolocateControl({
 *     positionOptions: {
 *         enableHighAccuracy: true
 *     },
 *     trackUserLocation: true
 * }));
 * ```
 * @see [Locate the user](https://maplibre.org/maplibre-gl-js/docs/examples/locate-user/)
 *
 * ## Events
 *
 * **Event** `trackuserlocationend` of type {@link Event} will be fired when the `GeolocateControl` changes to the background state, which happens when a user changes the camera during an active position lock. This only applies when `trackUserLocation` is `true`. In the background state, the dot on the map will update with location updates but the camera will not.
 *
 * **Event** `trackuserlocationstart` of type {@link Event} will be fired when the `GeolocateControl` changes to the active lock state, which happens either upon first obtaining a successful Geolocation API position for the user (a `geolocate` event will follow), or the user clicks the geolocate button when in the background state which uses the last known position to recenter the map and enter active lock state (no `geolocate` event will follow unless the users's location changes).
 *
 * **Event** `userlocationlostfocus` of type {@link Event} will be fired when the `GeolocateControl` changes to the background state, which happens when a user changes the camera during an active position lock. This only applies when `trackUserLocation` is `true`. In the background state, the dot on the map will update with location updates but the camera will not.
 *
 * **Event** `userlocationfocus` of type {@link Event} will be fired when the `GeolocateControl` changes to the active lock state, which happens upon the user clicks the geolocate button when in the background state which uses the last known position to recenter the map and enter active lock state.
 *
 * **Event** `geolocate` of type {@link Event} will be fired on each Geolocation API position update which returned as success.
 * `data` - The returned [Position](https://developer.mozilla.org/en-US/docs/Web/API/Position) object from the callback in [Geolocation.getCurrentPosition()](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/getCurrentPosition) or [Geolocation.watchPosition()](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/watchPosition).
 *
 * **Event** `error` of type {@link Event} will be fired on each Geolocation API position update which returned as an error.
 * `data` - The returned [PositionError](https://developer.mozilla.org/en-US/docs/Web/API/PositionError) object from the callback in [Geolocation.getCurrentPosition()](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/getCurrentPosition) or [Geolocation.watchPosition()](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/watchPosition).
 *
 * **Event** `outofmaxbounds` of type {@link Event} will be fired on each Geolocation API position update which returned as success but user position is out of map `maxBounds`.
 * `data` - The returned [Position](https://developer.mozilla.org/en-US/docs/Web/API/Position) object from the callback in [Geolocation.getCurrentPosition()](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/getCurrentPosition) or [Geolocation.watchPosition()](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/watchPosition).
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when a trackuserlocationend event occurs.
 * geolocate.on('trackuserlocationend', () => {
 *   console.log('A trackuserlocationend event has occurred.')
 * });
 * ```
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when a trackuserlocationstart event occurs.
 * geolocate.on('trackuserlocationstart', () => {
 *   console.log('A trackuserlocationstart event has occurred.')
 * });
 * ```
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when an userlocationlostfocus event occurs.
 * geolocate.on('userlocationlostfocus', function() {
 *   console.log('An userlocationlostfocus event has occurred.')
 * });
 * ```
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when an userlocationfocus event occurs.
 * geolocate.on('userlocationfocus', function() {
 *   console.log('An userlocationfocus event has occurred.')
 * });
 * ```
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when a geolocate event occurs.
 * geolocate.on('geolocate', () => {
 *   console.log('A geolocate event has occurred.')
 * });
 * ```
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when an error event occurs.
 * geolocate.on('error', () => {
 *   console.log('An error event has occurred.')
 * });
 * ```
 *
 * @example
 * ```ts
 * // Initialize the geolocate control.
 * let geolocate = new GeolocateControl({
 *   positionOptions: {
 *       enableHighAccuracy: true
 *   },
 *   trackUserLocation: true
 * });
 * // Add the control to the map.
 * map.addControl(geolocate);
 * // Set an event listener that fires
 * // when an outofmaxbounds event occurs.
 * geolocate.on('outofmaxbounds', () => {
 *   console.log('An outofmaxbounds event has occurred.')
 * });
 * ```
 */
external class GeolocateControl: Evented, IControl {
  constructor()

  /**
   * @param options - the control's options
   */
  constructor(options: GeolocateControlOptions?)

  override fun onAdd(map: Map): HTMLElement

  override fun onRemove(map: Map)

  override val getDefaultPosition: (() -> String)?

  /**
   * Programmatically request and move the map to the user's location.
   *
   * @returns `false` if called before control was added to a map, otherwise returns `true`.
   * @example
   * ```ts
   * // Initialize the geolocate control.
   * let geolocate = new GeolocateControl({
   *  positionOptions: {
   *    enableHighAccuracy: true
   *  },
   *  trackUserLocation: true
   * });
   * // Add the control to the map.
   * map.addControl(geolocate);
   * map.on('load', () => {
   *   geolocate.trigger();
   * });
   * ```
   */
  fun trigger(): Boolean
}