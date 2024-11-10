@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject

import net.redyeti.maplibre.*
import net.redyeti.maplibre.jsobject.stylespec.SourceSpecification
import net.redyeti.maplibre.jsobject.stylespec.CanvasSourceSpecification
import net.redyeti.maplibre.jsobject.geojson.MapGeoJSONFeature
import org.w3c.dom.*
import org.w3c.fetch.RequestCache

external class Event(type: String, data: Any? = definedExternally) {
  val type: String
}

/**
 * Methods mixed in to other classes for event capabilities.
 *
 * @group Event Related
 */
open external class Evented {
  var _listeners: Listeners
  var _oneTimeListeners: Listeners
  var _eventedParent: Evented
  var _eventedParentData: Any? /* any | (() => any) */

  /**
   * Adds a listener to a specified event type.
   *
   * @param type - The event type to add a listen for.
   * @param listener - The function to be called when the event is fired.
   * The listener function is called with the data object passed to `fire`,
   * extended with `target` and `type` properties.
   * @returns `this`
   */
  fun on(type: String, listener: (a: Any?) -> Any?): Evented

  /**
   * Removes a previously registered event listener.
   *
   * @param type - The event type to remove listeners for.
   * @param listener - The listener function to remove.
   * @returns `this`
   */
  fun off(type: String, listener: (a: Any?) -> Any?): Evented

  /**
   * Adds a listener that will be called only once to a specified event type.
   *
   * The listener will be called first time the event fires after the listener is registered.
   *
   * @param type - The event type to listen for.
   * @param listener - The function to be called when the event is fired the first time.
   * @returns `this` or a promise if a listener is not provided
   */
  //fun once(type: String): Promise<Any>
  fun once(type: String, listener: (a: Any) -> Any = definedExternally): Evented
  fun fire(event: Event, properties: Any? = definedExternally): Evented

  fun fire(event: String, properties: Any? = definedExternally): Evented

  /**
   * Returns a true if this instance of Evented or any forwardeed instances of Evented have a listener for the specified type.
   *
   * @param type - The event type
   * @returns `true` if there is at least one registered listener for specified event type, `false` otherwise
   */
  fun listens(type: String): Boolean

  /**
   * Bubble all events fired by this instance of Evented to this parent instance of Evented.
   * @returns `this`
   */
  fun setEventedParent(parent: Evented? = definedExternally, data: Any? = definedExternally): Evented

  fun setEventedParent(parent: Evented? = definedExternally, data: () -> Any? = definedExternally): Evented
}

open external class Camera : Evented {
  constructor (transform: Transform, options: CameraOptions)

  var transform: Transform

  // var terrain: Terrain
  var _moving: Boolean
  var _zooming: Boolean
  var _rotating: Boolean
  var _pitching: Boolean
  var _padding: Boolean
  var _bearingSnap: Double
  var _easeStart: Double

  // var _easeOptions: Camera_easeOptions
  var _easeId: Any /* string | void */
  var _onEaseFrame: (_: Double) -> Unit
  var _onEaseEnd: (easeId: String? /* use undefined for default */) -> Unit
  // var _easeFrameId: TaskID
  var _easeFrameId: Double
  /**
   * @internal
   * holds the geographical coordinate of the target
   */
  var _elevationCenter: LngLat

  /**
   * @internal
   * holds the targ altitude value, = center elevation of the target.
   * This value may changes during flight, because new terrain-tiles loads during flight.
   */
  var _elevationTarget: Double

  /**
   * @internal
   * holds the start altitude value, = center elevation before animation begins
   * this value will recalculated during flight in respect of changing _elevationTarget values,
   * so the linear interpolation between start and target keeps smooth and without jumps.
   */
  var _elevationStart: Double

  /**
   * @internal
   * Saves the current state of the elevation freeze - this is used during map movement to prevent "rocky" camera movement.
   */
  var _elevationFreeze: Boolean

  /**
   * @internal
   * Used to track accumulated changes during continuous interaction
   */
  var _requestedCameraState: Transform?

  /**
   * A callback used to defer camera updates or apply arbitrary constraints.
   * If specified, this Camera instance can be used as a stateless component in React etc.
   */
  var transformCameraUpdate: (next: CameraUpdateTransformFunctionNext) -> CameraUpdateTransformFunctionNext?
  // fun _requestRenderFrame(a: () -> Unit): TaskID
  // fun _cancelRenderFrame(t: TaskID): Unit
  fun _requestRenderFrame(a: () -> Unit): Double
  fun _cancelRenderFrame(t: Double)

  /**
   * Returns the map's geographical centerpoint.
   *
   * @returns The map's geographical centerpoint.
   * @example
   * Return a LngLat object such as `{lng: 0, lat: 0}`
   * ```ts
   * let center = map.getCenter();
   * // access longitude and latitude values directly
   * let {lng, lat} = map.getCenter();
   * ```
   */
  fun getCenter(): LngLat

  /**
   * Sets the map's geographical centerpoint. Equivalent to `jumpTo({center: center})`.
   *
   * Triggers the following events: `movestart` and `moveend`.
   *
   * @param center - The centerpoint to set.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * map.setCenter([-74, 38]);
   * ```
   */
  fun setCenter(center: LngLat, eventData: Any? = definedExternally): Camera

  /**
   * Pans the map by the specified offset.
   *
   * Triggers the following events: `movestart` and `moveend`.
   *
   * @param offset - `x` and `y` coordinates by which to pan the map.
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @see [Navigate the map with game-like controls](https://maplibre.org/maplibre-gl-js/docs/examples/game-controls/)
   */
  fun panBy(
    offset: Point,
    options: AnimationOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Pans the map to the specified location with an animated transition.
   *
   * Triggers the following events: `movestart` and `moveend`.
   *
   * @param lnglat - The location to pan the map to.
   * @param options - Options describing the destination and animation of the transition.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * map.panTo([-74, 38]);
   * // Specify that the panTo animation should last 5000 milliseconds.
   * map.panTo([-74, 38], {duration: 5000});
   * ```
   * @see [Update a feature in realtime](https://maplibre.org/maplibre-gl-js/docs/examples/live-update-feature/)
   */
  fun panTo(
    lnglat: LngLat,
    options: AnimationOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Returns the map's current zoom level.
   *
   * @returns The map's current zoom level.
   * @example
   * ```ts
   * map.getZoom();
   * ```
   */
  fun getZoom(): Double

  /**
   * Sets the map's zoom level. Equivalent to `jumpTo({zoom: zoom})`.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, and `zoomend`.
   *
   * @param zoom - The zoom level to set (0-20).
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * Zoom to the zoom level 5 without an animated transition
   * ```ts
   * map.setZoom(5);
   * ```
   */
  fun setZoom(zoom: Double, eventData: Any? = definedExternally): Camera

  /**
   * Zooms the map to the specified zoom level, with an animated transition.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, and `zoomend`.
   *
   * @param zoom - The zoom level to transition to.
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * // Zoom to the zoom level 5 without an animated transition
   * map.zoomTo(5);
   * // Zoom to the zoom level 8 with an animated transition
   * map.zoomTo(8, {
   *   duration: 2000,
   *   offset: [100, 50]
   * });
   * ```
   */
  fun zoomTo(
    zoom: Double,
    options: AnimationOptions? = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Increases the map's zoom level by 1.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, and `zoomend`.
   *
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * Zoom the map in one level with a custom animation duration
   * ```ts
   * map.zoomIn({duration: 1000});
   * ```
   */
  fun zoomIn(options: AnimationOptions = definedExternally, eventData: Any? = definedExternally): Camera

  /**
   * Decreases the map's zoom level by 1.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, and `zoomend`.
   *
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * Zoom the map out one level with a custom animation offset
   * ```ts
   * map.zoomOut({offset: [80, 60]});
   * ```
   */
  fun zoomOut(options: AnimationOptions = definedExternally, eventData: Any? = definedExternally): Camera

  /**
   * Returns the map's current bearing. The bearing is the compass direction that is "up"; for example, a bearing
   * of 90° orients the map so that east is up.
   *
   * @returns The map's current bearing.
   * @see [Navigate the map with game-like controls](https://maplibre.org/maplibre-gl-js/docs/examples/game-controls/)
   */
  fun getBearing(): Double

  /**
   * Sets the map's bearing (rotation). The bearing is the compass direction that is "up"; for example, a bearing
   * of 90° orients the map so that east is up.
   *
   * Equivalent to `jumpTo({bearing: bearing})`.
   *
   * Triggers the following events: `movestart`, `moveend`, and `rotate`.
   *
   * @param bearing - The desired bearing.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * Rotate the map to 90 degrees
   * ```ts
   * map.setBearing(90);
   * ```
   */
  fun setBearing(bearing: Double, eventData: Any? = definedExternally): Camera

  /**
   * Returns the current padding applied around the map viewport.
   *
   * @returns The current padding around the map viewport.
   */
  fun getPadding(): PaddingOptions

  /**
   * Sets the padding in pixels around the viewport.
   *
   * Equivalent to `jumpTo({padding: padding})`.
   *
   * Triggers the following events: `movestart` and `moveend`.
   *
   * @param padding - The desired padding.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * Sets a left padding of 300px, and a top padding of 50px
   * ```ts
   * map.setPadding({ left: 300, top: 50 });
   * ```
   */
  fun setPadding(padding: PaddingOptions, eventData: Any? = definedExternally): Camera

  /**
   * Rotates the map to the specified bearing, with an animated transition. The bearing is the compass direction
   * that is "up"; for example, a bearing of 90° orients the map so that east is up.
   *
   * Triggers the following events: `movestart`, `moveend`, and `rotate`.
   *
   * @param bearing - The desired bearing.
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   */
  fun rotateTo(
    bearing: Double,
    options: AnimationOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Rotates the map so that north is up (0° bearing), with an animated transition.
   *
   * Triggers the following events: `movestart`, `moveend`, and `rotate`.
   *
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   */
  fun resetNorth(options: AnimationOptions = definedExternally, eventData: Any? = definedExternally): Camera

  /**
   * Rotates and pitches the map so that north is up (0° bearing) and pitch is 0°, with an animated transition.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `pitchstart`, `pitch`, `pitchend`, and `rotate`.
   *
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   */
  fun resetNorthPitch(
    options: AnimationOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Snaps the map so that north is up (0° bearing), if the current bearing is close enough to it (i.e. within the
   * `bearingSnap` threshold).
   *
   * Triggers the following events: `movestart`, `moveend`, and `rotate`.
   *
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   */
  fun snapToNorth(options: AnimationOptions = definedExternally, eventData: Any? = definedExternally): Camera

  /**
   * Returns the map's current pitch (tilt).
   *
   * @returns The map's current pitch, measured in degrees away from the plane of the screen.
   */
  fun getPitch(): Double

  /**
   * Sets the map's pitch (tilt). Equivalent to `jumpTo({pitch: pitch})`.
   *
   * Triggers the following events: `movestart`, `moveend`, `pitchstart`, and `pitchend`.
   *
   * @param pitch - The pitch to set, measured in degrees away from the plane of the screen (0-60).
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   */
  fun setPitch(pitch: Double, eventData: Any? = definedExternally): Camera

  /**
   * @param bounds - Calculate the center for these bounds in the viewport and use
   * the highest zoom level up to and including `Map#getMaxZoom()` that fits
   * in the viewport. LngLatBounds represent a box that is always axis-aligned with bearing 0.
   * @param options - Options object
   * @returns If map is able to fit to provided bounds, returns `center`, `zoom`, and `bearing`.
   * If map is unable to fit, method will warn and return undefined.
   * @example
   * ```ts
   * let bbox = [[-79, 43], [-73, 45]];
   * let newCameraTransform = map.cameraForBounds(bbox, {
   *   padding: {top: 10, bottom:25, left: 15, right: 5}
   * });
   * ```
   */
  fun cameraForBounds(bounds: LngLatBounds, options: CameraForBoundsOptions = definedExternally): CenterZoomBearing

  /**
   * @internal
   * Calculate the center of these two points in the viewport and use
   * the highest zoom level up to and including `Map#getMaxZoom()` that fits
   * the points in the viewport at the specified bearing.
   * @param p0 - First point
   * @param p1 - Second point
   * @param bearing - Desired map bearing at end of animation, in degrees
   * @param options - the camera options
   * @returns If map is able to fit to provided bounds, returns `center`, `zoom`, and `bearing`.
   *      If map is unable to fit, method will warn and return undefined.
   * @example
   * ```ts
   * let p0 = [-79, 43];
   * let p1 = [-73, 45];
   * let bearing = 90;
   * let newCameraTransform = map._cameraForBoxAndBearing(p0, p1, bearing, {
   *   padding: {top: 10, bottom:25, left: 15, right: 5}
   * });
   * ```
   */
  fun _cameraForBoxAndBearing(
    p0: LngLat,
    p1: LngLat,
    bearing: Double,
    options: CameraForBoundsOptions = definedExternally
  ): CenterZoomBearing

  /**
   * Pans and zooms the map to contain its visible area within the specified geographical bounds.
   * This function will also reset the map's bearing to 0 if bearing is nonzero.
   *
   * Triggers the following events: `movestart` and `moveend`.
   *
   * @param bounds - Center these bounds in the viewport and use the highest
   * zoom level up to and including `Map#getMaxZoom()` that fits them in the viewport.
   * @param options - Options supports all properties from {@link AnimationOptions} and {@link CameraOptions} in addition to the fields below.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * let bbox = [[-79, 43], [-73, 45]];
   * map.fitBounds(bbox, {
   *   padding: {top: 10, bottom:25, left: 15, right: 5}
   * });
   * ```
   * @see [Fit a map to a bounding box](https://maplibre.org/maplibre-gl-js/docs/examples/fitbounds/)
   */
  fun fitBounds(
    bounds: LngLatBounds,
    options: FitBoundsOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Pans, rotates and zooms the map to to fit the box made by points p0 and p1
   * once the map is rotated to the specified bearing. To zoom without rotating,
   * pass in the current map bearing.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, `zoomend` and `rotate`.
   *
   * @param p0 - First point on screen, in pixel coordinates
   * @param p1 - Second point on screen, in pixel coordinates
   * @param bearing - Desired map bearing at end of animation, in degrees
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * let p0 = [220, 400];
   * let p1 = [500, 900];
   * map.fitScreenCoordinates(p0, p1, map.getBearing(), {
   *   padding: {top: 10, bottom:25, left: 15, right: 5}
   * });
   * ```
   * @see Used by {@link BoxZoomHandler}
   */
  fun fitScreenCoordinates(
    p0: Point,
    p1: Point,
    bearing: Double,
    options: FitBoundsOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  fun _fitInternal(
    calculatedOptions: CenterZoomBearing = definedExternally,
    options: FitBoundsOptions = definedExternally,
    eventData: Any? = definedExternally
  ): Camera

  /**
   * Changes any combination of center, zoom, bearing, and pitch, without
   * an animated transition. The map will retain its current values for any
   * details not specified in `options`.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, `zoomend`, `pitchstart`,
   * `pitch`, `pitchend`, and `rotate`.
   *
   * @param options - Options object
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * // jump to coordinates at current zoom
   * map.jumpTo({center: [0, 0]});
   * // jump with zoom, pitch, and bearing options
   * map.jumpTo({
   *   center: [0, 0],
   *   zoom: 8,
   *   pitch: 45,
   *   bearing: 90
   * });
   * ```
   * @see [Jump to a series of locations](https://maplibre.org/maplibre-gl-js/docs/examples/jump-to/)
   * @see [Update a feature in realtime](https://maplibre.org/maplibre-gl-js/docs/examples/live-update-feature/)
   */
  fun jumpTo(options: JumpToOptions, eventData: Any? = definedExternally): Camera

  /**
   * Calculates pitch, zoom and bearing for looking at `newCenter` with the camera position being `newCenter`
   * and returns them as {@link CameraOptions}.
   * @param from - The camera to look from
   * @param altitudeFrom - The altitude of the camera to look from
   * @param to - The center to look at
   * @param altitudeTo - Optional altitude of the center to look at. If none given the ground height will be used.
   * @returns the calculated camera options
   */
  open fun calculateCameraOptionsFromTo(
    from: LngLat,
    altitudeFrom: Double,
    to: LngLat,
    altitudeTo: Double = definedExternally
  ): CameraOptions

  /**
   * Changes any combination of `center`, `zoom`, `bearing`, `pitch`, and `padding` with an animated transition
   * between old and new values. The map will retain its current values for any
   * details not specified in `options`.
   *
   * Note: The transition will happen instantly if the user has enabled
   * the `reduced motion` accessibility feature enabled in their operating system,
   * unless `options` includes `essential: true`.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, `zoomend`, `pitchstart`,
   * `pitch`, `pitchend`, and `rotate`.
   *
   * @param options - Options describing the destination and animation of the transition.
   * Accepts {@link CameraOptions} and {@link AnimationOptions}.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @see [Navigate the map with game-like controls](https://maplibre.org/maplibre-gl-js/docs/examples/game-controls/)
   */
  fun easeTo(options: CameraEaseToOptions, eventData: Any? = definedExternally): Camera
  fun _prepareEase(eventData: Any?, noMoveStart: Boolean, currently: Any? = definedExternally): Unit
  fun _prepareElevation(center: LngLat): Unit
  fun _updateElevation(k: Double): Unit
  fun _finalizeElevation(): Unit

  /**
   * @internal
   * Called when the camera is about to be manipulated.
   * If `transformCameraUpdate` is specified, a copy of the current transform is created to track the accumulated changes.
   * This underlying transform represents the "desired state" proposed by input handlers / animations / UI controls.
   * It may differ from the state used for rendering (`this.transform`).
   * @returns Transform to apply changes to
   */
  fun _getTransformForUpdate(): Transform

  /**
   * @internal
   * Called after the camera is done being manipulated.
   * @param tr - the requested camera end state
   * Call `transformCameraUpdate` if present, and then apply the "approved" changes.
   */
  fun _applyUpdatedTransform(tr: Transform): Unit
  fun _fireMoveEvents(eventData: Any? = definedExternally): Unit
  fun _afterEase(eventData: Any? = definedExternally, easeId: String = definedExternally): Unit

  /**
   * Changes any combination of center, zoom, bearing, and pitch, animating the transition along a curve that
   * evokes flight. The animation seamlessly incorporates zooming and panning to help
   * the user maintain her bearings even after traversing a great distance.
   *
   * Note: The animation will be skipped, and this will behave equivalently to `jumpTo`
   * if the user has the `reduced motion` accessibility feature enabled in their operating system,
   * unless 'options' includes `essential: true`.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, `zoomstart`, `zoom`, `zoomend`, `pitchstart`,
   * `pitch`, `pitchend`, and `rotate`.
   *
   * @param options - Options describing the destination and animation of the transition.
   * Accepts {@link CameraOptions}, {@link AnimationOptions},
   * and the following additional options.
   * @param eventData - Additional properties to be added to event objects of events triggered by this method.
   * @returns `this`
   * @example
   * ```ts
   * // fly with default options to null island
   * map.flyTo({center: [0, 0], zoom: 9});
   * // using flyTo options
   * map.flyTo({
   *   center: [0, 0],
   *   zoom: 9,
   *   speed: 0.2,
   *   curve: 1,
   *   easing(t) {
   *     return t;
   *   }
   * });
   * ```
   * @see [Fly to a location](https://maplibre.org/maplibre-gl-js/docs/examples/flyto/)
   * @see [Slowly fly to a location](https://maplibre.org/maplibre-gl-js/docs/examples/flyto-options/)
   * @see [Fly to a location based on scroll position](https://maplibre.org/maplibre-gl-js/docs/examples/scroll-fly-to/)
   */
  fun flyTo(options: FlyToOptions, eventData: Any? = definedExternally): Camera
  fun isEasing(): Boolean

  /**
   * Stops any animated transition underway.
   *
   * @returns `this`
   */
  fun stop(): Camera
  fun _stop(allowGestures: Boolean = definedExternally, easeId: String = definedExternally): Camera
  fun _ease(frame: (_: Double) -> Unit, finish: () -> Unit, options: Camera_easeOptions): Unit
  var _renderFrameCallback: () -> Unit
  fun _normalizeBearing(bearing: Double, currentBearing: Double): Double
  fun _normalizeCenter(center: LngLat): Unit

  /**
   * Query the current elevation of location. Returns `null` if terrain is not enabled. Elevation is in meters relative to mean sea-level.
   * @param LngLat - [x,y] or LngLat coordinates of the location
   * @returns elevation in meters
   */
  fun queryTerrainElevation(LngLat: LngLat): Double?
}

/**
 * The `Map` object represents the map on your page. It exposes methods
 * and properties that enable you to programmatically change the map,
 * and fires events as users interact with it.
 *
 * You create a `Map` by specifying a `container` and other options, see {@link MapOptions} for the full list.
 * Then MapLibre GL JS initializes the map on the page and returns your `Map` object.
 *
 * @group Main
 *
 * @example
 * ```ts
 * let map = new Map({
 *   container: 'map',
 *   center: [-122.420679, 37.772537],
 *   zoom: 13,
 *   style: style_object,
 *   hash: true,
 *   transformRequest: (url, resourceType)=> {
 *     if(resourceType === 'Source' && url.startsWith('http://myHost')) {
 *       return {
 *        url: url.replace('http', 'https'),
 *        headers: { 'my-custom-header': true},
 *        credentials: 'include'  // Include cookies for cross-origin requests
 *      }
 *     }
 *   }
 * });
 * ```
 * @see [Display a map](https://maplibre.org/maplibre-gl-js/docs/examples/simple-map/)
 */
external class Map(options: MapOptions) {
  // var style: Style
  // var painter: Painter
  // var handlers: HandlerManager
  var _container: HTMLElement
  var _canvasContainer: HTMLElement
  var _controlContainer: HTMLElement
  var _controlPositions: String // TODO: Map$1_controlPositions  https://github.com/maplibre/maplibre-gl-js/blob/3bea9a10a/src/ui/control/control.ts#L7
  var _interactive: Boolean
  var _showTileBoundaries: Boolean
  var _showCollisionBoxes: Boolean
  var _showPadding: Boolean
  var _showOverdrawInspector: Boolean
  var _repaint: Boolean
  var _vertices: Boolean
  var _canvas: HTMLCanvasElement
  var _maxTileCacheSize: Double
  var _maxTileCacheZoomLevels: Double

  // var _frameRequest: AbortController   // TODO
  var _styleDirty: Boolean
  var _sourcesDirty: Boolean
  var _placementDirty: Boolean
  var _loaded: Boolean
  var _idleTriggered: Boolean
  var _fullyLoaded: Boolean
  var _trackResize: Boolean

  // var _resizeObserver: ResizeObserver  // TODO
  var _preserveDrawingBuffer: Boolean
  var _failIfMajorPerformanceCaveat: Boolean
  var _antialias: Boolean
  var _refreshExpiredTiles: Boolean

  // var _hash: Hash
  var _delegatedListeners: Any?
  var _fadeDuration: Double
  var _crossSourceCollisions: Boolean
  var _crossFadingFactor: Double
  var _collectResourceTiming: Boolean
  var _renderTaskQueue: TaskQueue
  var _controls: Array<IControl>
  var _mapId: Double
  var _localIdeographFontFamily: String
  var _validateStyle: Boolean
  var _requestManager: RequestManager
  var _locale: Any                        // TODO: external interface Map$1_locale
  var _removed: Boolean
  var _clickTolerance: Double
  var _overridePixelRatio: Double?
  var _maxCanvasSize: Array<Int>
  var _terrainDataCallback: (e: Any /* MapStyleDataEvent | MapSourceDataEvent */) -> Unit

  /**
   * @internal
   * image queue throttling handle. To be used later when clean up
   */
  var _imageQueueHandle: Double
  /**
   * The map's {@link ScrollZoomHandler}, which implements zooming in and out with a scroll wheel or trackpad.
   * Find more details and examples using `scrollZoom` in the {@link ScrollZoomHandler} section.
   */
  // var scrollZoom: ScrollZoomHandler
  /**
   * The map's {@link BoxZoomHandler}, which implements zooming using a drag gesture with the Shift key pressed.
   * Find more details and examples using `boxZoom` in the {@link BoxZoomHandler} section.
   */
  // var boxZoom: BoxZoomHandler
  /**
   * The map's {@link DragRotateHandler}, which implements rotating the map while dragging with the right
   * mouse button or with the Control key pressed. Find more details and examples using `dragRotate`
   * in the {@link DragRotateHandler} section.
   */
  // var dragRotate: DragRotateHandler
  /**
   * The map's {@link DragPanHandler}, which implements dragging the map with a mouse or touch gesture.
   * Find more details and examples using `dragPan` in the {@link DragPanHandler} section.
   */
  // var dragPan: DragPanHandler
  /**
   * The map's {@link KeyboardHandler}, which allows the user to zoom, rotate, and pan the map using keyboard
   * shortcuts. Find more details and examples using `keyboard` in the {@link KeyboardHandler} section.
   */
  // var keyboard: KeyboardHandler
  /**
   * The map's {@link DoubleClickZoomHandler}, which allows the user to zoom by double clicking.
   * Find more details and examples using `doubleClickZoom` in the {@link DoubleClickZoomHandler} section.
   */
  // var doubleClickZoom: DoubleClickZoomHandler
  /**
   * The map's {@link TwoFingersTouchZoomRotateHandler}, which allows the user to zoom or rotate the map with touch gestures.
   * Find more details and examples using `touchZoomRotate` in the {@link TwoFingersTouchZoomRotateHandler} section.
   */
  // var touchZoomRotate: TwoFingersTouchZoomRotateHandler
  /**
   * The map's {@link TwoFingersTouchPitchHandler}, which allows the user to pitch the map with touch gestures.
   * Find more details and examples using `touchPitch` in the {@link TwoFingersTouchPitchHandler} section.
   */
  // var touchPitch: TwoFingersTouchPitchHandler
  /**
   * The map's {@link CooperativeGesturesHandler}, which allows the user to see cooperative gesture info when user tries to zoom in/out.
   * Find more details and examples using `cooperativeGestures` in the {@link CooperativeGesturesHandler} section.
   */
  // var cooperativeGestures: CooperativeGesturesHandler
  /**
   * @internal
   * Returns a unique number for this map instance which is used for the MapLoadEvent
   * to make sure we only fire one event per instantiated map object.
   * @returns the uniq map ID
   */
  fun _getMapId(): Double

  /**
   * Adds an {@link IControl} to the map, calling `control.onAdd(this)`.
   *
   * An {@link ErrorEvent} will be fired if the image parameter is invald.
   *
   * @param control - The {@link IControl} to add.
   * @param position - position on the map to which the control will be added.
   * Valid values are `'top-left'`, `'top-right'`, `'bottom-left'`, and `'bottom-right'`. Defaults to `'top-right'`.
   * @returns `this`
   * @example
   * Add zoom and rotation controls to the map.
   * ```ts
   * map.addControl(new NavigationControl());
   * ```
   * @see [Display map navigation controls](https://maplibre.org/maplibre-gl-js/docs/examples/navigation/)
   */
  // fun addControl(control: IControl, position: ControlPosition = definedExternally): Map
  fun addControl(control: IControl, position: String = definedExternally): Map

  /**
   * Removes the control from the map.
   *
   * An {@link ErrorEvent} will be fired if the image parameter is invald.
   *
   * @param control - The {@link IControl} to remove.
   * @returns `this`
   * @example
   * ```ts
   * // Define a new navigation control.
   * let navigation = new NavigationControl();
   * // Add zoom and rotation controls to the map.
   * map.addControl(navigation);
   * // Remove zoom and rotation controls from the map.
   * map.removeControl(navigation);
   * ```
   */
  fun removeControl(control: IControl): Map

  /**
   * Checks if a control exists on the map.
   *
   * @param control - The {@link IControl} to check.
   * @returns true if map contains control.
   * @example
   * ```ts
   * // Define a new navigation control.
   * let navigation = new NavigationControl();
   * // Add zoom and rotation controls to the map.
   * map.addControl(navigation);
   * // Check that the navigation control exists on the map.
   * map.hasControl(navigation);
   * ```
   */
  fun hasControl(control: IControl): Boolean
  fun calculateCameraOptionsFromTo(
    from: LngLat,
    altitudeFrom: Double,
    to: LngLat,
    altitudeTo: Double = definedExternally
  ): CameraOptions

  /**
   * Resizes the map according to the dimensions of its
   * `container` element.
   *
   * Checks if the map container size changed and updates the map if it has changed.
   * This method must be called after the map's `container` is resized programmatically
   * or when the map is shown after being initially hidden with CSS.
   *
   * Triggers the following events: `movestart`, `move`, `moveend`, and `resize`.
   *
   * @param eventData - Additional properties to be passed to `movestart`, `move`, `resize`, and `moveend`
   * events that get triggered as a result of resize. This can be useful for differentiating the
   * source of an event (for example, user-initiated or programmatically-triggered events).
   * @returns `this`
   * @example
   * Resize the map when the map container is shown after being initially hidden with CSS.
   * ```ts
   * let mapDiv = document.getElementById('map');
   * if (mapDiv.style.visibility === true) map.resize();
   * ```
   */
  fun resize(eventData: Any? = definedExternally): Map

  /**
   * @internal
   * Return the map's pixel ratio eventually scaled down to respect maxCanvasSize.
   * Internally you should use this and not getPixelRatio().
   */
  fun _getClampedPixelRatio(width: Double, height: Double): Double

  /**
   * Returns the map's pixel ratio.
   * Note that the pixel ratio actually applied may be lower to respect maxCanvasSize.
   * @returns The pixel ratio.
   */
  fun getPixelRatio(): Double

  /**
   * Sets the map's pixel ratio. This allows to override `devicePixelRatio`.
   * After this call, the canvas' `width` attribute will be `container.clientWidth * pixelRatio`
   * and its height attribute will be `container.clientHeight * pixelRatio`.
   * Set this to null to disable `devicePixelRatio` override.
   * Note that the pixel ratio actually applied may be lower to respect maxCanvasSize.
   * @param pixelRatio - The pixel ratio.
   */
  fun setPixelRatio(pixelRatio: Double): Map

  /**
   * Returns the map's geographical bounds. When the bearing or pitch is non-zero, the visible region is not
   * an axis-aligned rectangle, and the result is the smallest bounds that encompasses the visible region.
   * @returns The geographical bounds of the map as {@link LngLatBounds}.
   * @example
   * ```ts
   * let bounds = map.getBounds();
   * ```
   */
  fun getBounds(): LngLatBounds

  /**
   * Returns the maximum geographical bounds the map is constrained to, or `null` if none set.
   * @returns The map object.
   * @example
   * ```ts
   * let maxBounds = map.getMaxBounds();
   * ```
   */
  fun getMaxBounds(): LngLatBounds?

  /**
   * Sets or clears the map's geographical bounds.
   *
   * Pan and zoom operations are constrained within these bounds.
   * If a pan or zoom is performed that would
   * display regions outside these bounds, the map will
   * instead display a position and zoom level
   * as close as possible to the operation's request while still
   * remaining within the bounds.
   *
   * @param bounds - The maximum bounds to set. If `null` or `undefined` is provided, the function removes the map's maximum bounds.
   * @returns `this`
   * @example
   * Define bounds that conform to the `LngLatBoundsLike` object as set the max bounds.
   * ```ts
   * let bounds = [
   *   [-74.04728, 40.68392], // [west, south]
   *   [-73.91058, 40.87764]  // [east, north]
   * ];
   * map.setMaxBounds(bounds);
   * ```
   */
  fun setMaxBounds(bounds: LngLatBounds? = definedExternally): Map

  /**
   * Sets or clears the map's minimum zoom level.
   * If the map's current zoom level is lower than the new minimum,
   * the map will zoom to the new minimum.
   *
   * It is not always possible to zoom out and reach the set `minZoom`.
   * Other factors such as map height may restrict zooming. For example,
   * if the map is 512px tall it will not be possible to zoom below zoom 0
   * no matter what the `minZoom` is set to.
   *
   * A {@link ErrorEvent} event will be fired if minZoom is out of bounds.
   *
   * @param minZoom - The minimum zoom level to set (-2 - 24).
   * If `null` or `undefined` is provided, the function removes the current minimum zoom (i.e. sets it to -2).
   * @returns `this`
   * @example
   * ```ts
   * map.setMinZoom(12.25);
   * ```
   */
  fun setMinZoom(minZoom: Double? = definedExternally): Map

  /**
   * Returns the map's minimum allowable zoom level.
   *
   * @returns minZoom
   * @example
   * ```ts
   * let minZoom = map.getMinZoom();
   * ```
   */
  fun getMinZoom(): Double

  /**
   * Sets or clears the map's maximum zoom level.
   * If the map's current zoom level is higher than the new maximum,
   * the map will zoom to the new maximum.
   *
   * A {@link ErrorEvent} event will be fired if minZoom is out of bounds.
   *
   * @param maxZoom - The maximum zoom level to set.
   * If `null` or `undefined` is provided, the function removes the current maximum zoom (sets it to 22).
   * @returns `this`
   * @example
   * ```ts
   * map.setMaxZoom(18.75);
   * ```
   */
  fun setMaxZoom(maxZoom: Double? = definedExternally): Map

  /**
   * Returns the map's maximum allowable zoom level.
   *
   * @returns The maxZoom
   * @example
   * ```ts
   * let maxZoom = map.getMaxZoom();
   * ```
   */
  fun getMaxZoom(): Double

  /**
   * Sets or clears the map's minimum pitch.
   * If the map's current pitch is lower than the new minimum,
   * the map will pitch to the new minimum.
   *
   * A {@link ErrorEvent} event will be fired if minPitch is out of bounds.
   *
   * @param minPitch - The minimum pitch to set (0-85). Values greater than 60 degrees are experimental and may result in rendering issues. If you encounter any, please raise an issue with details in the MapLibre project.
   * If `null` or `undefined` is provided, the function removes the current minimum pitch (i.e. sets it to 0).
   * @returns `this`
   */
  fun setMinPitch(minPitch: Double? = definedExternally): Map

  /**
   * Returns the map's minimum allowable pitch.
   *
   * @returns The minPitch
   */
  fun getMinPitch(): Double

  /**
   * Sets or clears the map's maximum pitch.
   * If the map's current pitch is higher than the new maximum,
   * the map will pitch to the new maximum.
   *
   * A {@link ErrorEvent} event will be fired if maxPitch is out of bounds.
   *
   * @param maxPitch - The maximum pitch to set (0-85). Values greater than 60 degrees are experimental and may result in rendering issues. If you encounter any, please raise an issue with details in the MapLibre project.
   * If `null` or `undefined` is provided, the function removes the current maximum pitch (sets it to 60).
   * @returns `this`
   */
  fun setMaxPitch(maxPitch: Double? = definedExternally): Map

  /**
   * Returns the map's maximum allowable pitch.
   *
   * @returns The maxPitch
   */
  fun getMaxPitch(): Double

  /**
   * Returns the state of `renderWorldCopies`. If `true`, multiple copies of the world will be rendered side by side beyond -180 and 180 degrees longitude. If set to `false`:
   *
   * - When the map is zoomed out far enough that a single representation of the world does not fill the map's entire
   * container, there will be blank space beyond 180 and -180 degrees longitude.
   * - Features that cross 180 and -180 degrees longitude will be cut in two (with one portion on the right edge of the
   * map and the other on the left edge of the map) at every zoom level.
   * @returns The renderWorldCopies
   * @example
   * ```ts
   * let worldCopiesRendered = map.getRenderWorldCopies();
   * ```
   * @see [Render world copies](https://maplibre.org/maplibre-gl-js/docs/examples/render-world-copies/)
   */
  fun getRenderWorldCopies(): Boolean

  /**
   * Sets the state of `renderWorldCopies`.
   *
   * @param renderWorldCopies - If `true`, multiple copies of the world will be rendered side by side beyond -180 and 180 degrees longitude. If set to `false`:
   *
   * - When the map is zoomed out far enough that a single representation of the world does not fill the map's entire
   * container, there will be blank space beyond 180 and -180 degrees longitude.
   * - Features that cross 180 and -180 degrees longitude will be cut in two (with one portion on the right edge of the
   * map and the other on the left edge of the map) at every zoom level.
   *
   * `undefined` is treated as `true`, `null` is treated as `false`.
   * @returns `this`
   * @example
   * ```ts
   * map.setRenderWorldCopies(true);
   * ```
   * @see [Render world copies](https://maplibre.org/maplibre-gl-js/docs/examples/render-world-copies/)
   */
  fun setRenderWorldCopies(renderWorldCopies: Boolean? = definedExternally): Map

  /**
   * Returns a [Point](https://github.com/mapbox/point-geometry) representing pixel coordinates, relative to the map's `container`,
   * that correspond to the specified geographical location.
   *
   * @param lnglat - The geographical location to project.
   * @returns The [Point](https://github.com/mapbox/point-geometry) corresponding to `lnglat`, relative to the map's `container`.
   * @example
   * ```ts
   * let coordinate = [-122.420679, 37.772537];
   * let point = map.project(coordinate);
   * ```
   */
  fun project(lnglat: LngLat): Point

  /**
   * Returns a {@link LngLat} representing geographical coordinates that correspond
   * to the specified pixel coordinates.
   *
   * @param point - The pixel coordinates to unproject.
   * @returns The {@link LngLat} corresponding to `point`.
   * @example
   * ```ts
   * map.on('click', function(e) {
   *   // When the map is clicked, get the geographic coordinate.
   *   let coordinate = map.unproject(e.point);
   * });
   * ```
   */
  fun unproject(point: Point): LngLat

  /**
   * Returns true if the map is panning, zooming, rotating, or pitching due to a camera animation or user gesture.
   * @returns true if the map is moving.
   * @example
   * ```ts
   * let isMoving = map.isMoving();
   * ```
   */
  fun isMoving(): Boolean

  /**
   * Returns true if the map is zooming due to a camera animation or user gesture.
   * @returns true if the map is zooming.
   * @example
   * ```ts
   * let isZooming = map.isZooming();
   * ```
   */
  fun isZooming(): Boolean

  /**
   * Returns true if the map is rotating due to a camera animation or user gesture.
   * @returns true if the map is rotating.
   * @example
   * ```ts
   * map.isRotating();
   * ```
   */
  fun isRotating(): Boolean
  // fun _createDelegatedListener(type: /* keyof MapEventType */, layerId: String, listener: Listener): DelegatedListenerResult

  fun _createDelegatedListener(type: String, layerId: String, listener: (a: Any?) -> Any?): DelegatedListenerResult
  /**
   * @event
   * Adds a listener for events of a specified type, optionally limited to features in a specified style layer.
   * See {@link MapEventType} and {@link MapLayerEventType} for a full list of events and their description.
   *
   * | Event                  | Compatible with `layerId` |
   * |------------------------|---------------------------|
   * | `mousedown`            | yes                       |
   * | `mouseup`              | yes                       |
   * | `mouseover`            | yes                       |
   * | `mouseout`             | yes                       |
   * | `mousemove`            | yes                       |
   * | `mouseenter`           | yes (required)            |
   * | `mouseleave`           | yes (required)            |
   * | `click`                | yes                       |
   * | `dblclick`             | yes                       |
   * | `contextmenu`          | yes                       |
   * | `touchstart`           | yes                       |
   * | `touchend`             | yes                       |
   * | `touchcancel`          | yes                       |
   * | `wheel`                |                           |
   * | `resize`               |                           |
   * | `remove`               |                           |
   * | `touchmove`            |                           |
   * | `movestart`            |                           |
   * | `move`                 |                           |
   * | `moveend`              |                           |
   * | `dragstart`            |                           |
   * | `drag`                 |                           |
   * | `dragend`              |                           |
   * | `zoomstart`            |                           |
   * | `zoom`                 |                           |
   * | `zoomend`              |                           |
   * | `rotatestart`          |                           |
   * | `rotate`               |                           |
   * | `rotateend`            |                           |
   * | `pitchstart`           |                           |
   * | `pitch`                |                           |
   * | `pitchend`             |                           |
   * | `boxzoomstart`         |                           |
   * | `boxzoomend`           |                           |
   * | `boxzoomcancel`        |                           |
   * | `webglcontextlost`     |                           |
   * | `webglcontextrestored` |                           |
   * | `load`                 |                           |
   * | `render`               |                           |
   * | `idle`                 |                           |
   * | `error`                |                           |
   * | `data`                 |                           |
   * | `styledata`            |                           |
   * | `sourcedata`           |                           |
   * | `dataloading`          |                           |
   * | `styledataloading`     |                           |
   * | `sourcedataloading`    |                           |
   * | `styleimagemissing`    |                           |
   * | `dataabort`            |                           |
   * | `sourcedataabort`      |                           |
   *
   * @param type - The event type to listen for. Events compatible with the optional `layerId` parameter are triggered
   * when the cursor enters a visible portion of the specified layer from outside that layer or outside the map canvas.
   * @param layer - The ID of a style layer or a listener if no ID is provided. Event will only be triggered if its location
   * is within a visible feature in this layer. The event will have a `features` property containing
   * an array of the matching features. If `layer` is not supplied, the event will not have a `features` property.
   * Please note that many event types are not compatible with the optional `layer` parameter.
   * @param listener - The function to be called when the event is fired.
   * @returns `this`
   * @example
   * ```ts
   * // Set an event listener that will fire
   * // when the map has finished loading
   * map.on('load', function() {
   *   // Once the map has finished loading,
   *   // add a new layer
   *   map.addLayer({
   *     id: 'points-of-interest',
   *     source: {
   *       type: 'vector',
   *       url: 'https://maplibre.org/maplibre-style-spec/'
   *     },
   *     'source-layer': 'poi_label',
   *     type: 'circle',
   *     paint: {
   *       // MapLibre Style Specification paint properties
   *     },
   *     layout: {
   *       // MapLibre Style Specification layout properties
   *     }
   *   });
   * });
   * ```
   * @example
   * ```ts
   * // Set an event listener that will fire
   * // when a feature on the countries layer of the map is clicked
   * map.on('click', 'countries', (e) => {
   *   new Popup()
   *     .setLngLat(e.lngLat)
   *     .setHTML(`Country name: ${e.features[0].properties.name}`)
   *     .addTo(map);
   * });
   * ```
   * @see [Display popup on click](https://maplibre.org/maplibre-gl-js/docs/examples/popup-on-click/)
   * @see [Center the map on a clicked symbol](https://maplibre.org/maplibre-gl-js/docs/examples/center-on-symbol/)
   * @see [Create a hover effect](https://maplibre.org/maplibre-gl-js/docs/examples/hover-styles/)
   * @see [Create a draggable marker](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-point/)
   */
  //fun<T: MapLayerEventType> on(type: T, layer: String, listener: (ev: Any /* MapLayerEventType[T] & Object */) -> Unit): Map
  /**
   * Overload of the `on` method that allows to listen to events without specifying a layer.
   * @event
   * @param type - The type of the event.
   * @param listener - The listener callback.
   * @returns `this`
   */
  //fun<T: MapEventType> on(type: T, listener: (ev: Any /* MapEventType[T] & Object */) -> Unit): Map
  /**
   * Overload of the `on` method that allows to listen to events without specifying a layer.
   * @event
   * @param type - The type of the event.
   * @param listener - The listener callback.
   * @returns `this`
   */
  //fun on(type: MapEventType, listener: Listener): Map

  fun on(type: String, listener: (a: Any) -> Any): Map
  /**
   * Adds a listener that will be called only once to a specified event type, optionally limited to features in a specified style layer.
   *
   * @event
   * @param type - The event type to listen for; one of `'mousedown'`, `'mouseup'`, `'click'`, `'dblclick'`,
   * `'mousemove'`, `'mouseenter'`, `'mouseleave'`, `'mouseover'`, `'mouseout'`, `'contextmenu'`, `'touchstart'`,
   * `'touchend'`, or `'touchcancel'`. `mouseenter` and `mouseover` events are triggered when the cursor enters
   * a visible portion of the specified layer from outside that layer or outside the map canvas. `mouseleave`
   * and `mouseout` events are triggered when the cursor leaves a visible portion of the specified layer, or leaves
   * the map canvas.
   * @param layer - The ID of a style layer or a listener if no ID is provided. Only events whose location is within a visible
   * feature in this layer will trigger the listener. The event will have a `features` property containing
   * an array of the matching features.
   * @param listener - The function to be called when the event is fired.
   * @returns `this` if listener is provided, promise otherwise to allow easier usage of async/await
   */
  // fun <T : /* keyof MapLayerEventType */> once(type: T, layer: String, listener: (ev: Any /* MapLayerEventType[T] & Object */) -> Unit = definedExternally): Any /* this | Promise<MapLayerEventType[T] & Object> */
  /**
   * Overload of the `once` method that allows to listen to events without specifying a layer.
   * @event
   * @param type - The type of the event.
   * @param listener - The listener callback.
   * @returns `this`
   */
  // fun <T : /* keyof MapEventType */> once(type: T, listener: (ev: Any /* MapEventType[T] & Object */) -> Unit = definedExternally): Map
  /**
   * Overload of the `once` method that allows to listen to events without specifying a layer.
   * @event
   * @param type - The type of the event.
   * @param listener - The listener callback.
   * @returns `this`
   */
  // fun once(type: /* keyof MapEventType */, listener: Listener = definedExternally): Map

  fun once(type: String, listener: (a: Any?) -> Any? = definedExternally): Map
  /**
   * Removes an event listener for events previously added with `Map#on`.
   *
   * @event
   * @param type - The event type previously used to install the listener.
   * @param layer - The layer ID or listener previously used to install the listener.
   * @param listener - The function previously installed as a listener.
   * @returns `this`
   */
  // fun <T : /* keyof MapLayerEventType */> off(type: T, layer: String, listener: (ev: Any /* MapLayerEventType[T] & Object */) -> Unit): Map
  /**
   * Overload of the `off` method that allows to listen to events without specifying a layer.
   * @event
   * @param type - The type of the event.
   * @param listener - The function previously installed as a listener.
   * @returns `this`
   */
  // fun <T : /* keyof MapEventType */> off(type: T, listener: (ev: Any /* MapEventType[T] & Object */) -> Unit): Map
  /**
   * Overload of the `off` method that allows to listen to events without specifying a layer.
   * @event
   * @param type - The type of the event.
   * @param listener - The function previously installed as a listener.
   * @returns `this`
   */
  // fun off(type: /* keyof MapEventType */, listener: Listener): Map

  fun off(type: String, listener: (a: Any?) -> Any?): Map
  /**
   * Returns an array of MapGeoJSONFeature objects
   * representing visible features that satisfy the query parameters.
   *
   * @param geometryOrOptions - (optional) The geometry of the query region:
   * either a single point or southwest and northeast points describing a bounding box.
   * Omitting this parameter (i.e. calling {@link Map#queryRenderedFeatures} with zero arguments,
   * or with only a `options` argument) is equivalent to passing a bounding box encompassing the entire
   * map viewport.
   * The geometryOrOptions can receive a {@link QueryRenderedFeaturesOptions} only to support a situation where the function receives only one parameter which is the options parameter.
   * @param options - (optional) Options object.
   *
   * @returns An array of MapGeoJSONFeature objects.
   *
   * The `properties` value of each returned feature object contains the properties of its source feature. For GeoJSON sources, only
   * string and numeric property values are supported (i.e. `null`, `Array`, and `Object` values are not supported).
   *
   * Each feature includes top-level `layer`, `source`, and `sourceLayer` properties. The `layer` property is an object
   * representing the style layer to  which the feature belongs. Layout and paint properties in this object contain values
   * which are fully evaluated for the given zoom level and feature.
   *
   * Only features that are currently rendered are included. Some features will **not** be included, like:
   *
   * - Features from layers whose `visibility` property is `"none"`.
   * - Features from layers whose zoom range excludes the current zoom level.
   * - Symbol features that have been hidden due to text or icon collision.
   *
   * Features from all other layers are included, including features that may have no visible
   * contribution to the rendered result; for example, because the layer's opacity or color alpha component is set to
   * 0.
   *
   * The topmost rendered feature appears first in the returned array, and subsequent features are sorted by
   * descending z-order. Features that are rendered multiple times (due to wrapping across the antemeridian at low
   * zoom levels) are returned only once (though subject to the following caveat).
   *
   * Because features come from tiled vector data or GeoJSON data that is converted to tiles internally, feature
   * geometries may be split or duplicated across tile boundaries and, as a result, features may appear multiple
   * times in query results. For example, suppose there is a highway running through the bounding rectangle of a query.
   * The results of the query will be those parts of the highway that lie within the map tiles covering the bounding
   * rectangle, even if the highway extends into other tiles, and the portion of the highway within each map tile
   * will be returned as a separate feature. Similarly, a point feature near a tile boundary may appear in multiple
   * tiles due to tile buffering.
   *
   * @example
   * Find all features at a point
   * ```ts
   * let features = map.queryRenderedFeatures(
   *   [20, 35],
   *   { layers: ['my-layer-name'] }
   * );
   * ```
   *
   * @example
   * Find all features within a static bounding box
   * ```ts
   * let features = map.queryRenderedFeatures(
   *   [[10, 20], [30, 50]],
   *   { layers: ['my-layer-name'] }
   * );
   * ```
   *
   * @example
   * Find all features within a bounding box around a point
   * ```ts
   * let width = 10;
   * let height = 20;
   * let features = map.queryRenderedFeatures([
   *   [point.x - width / 2, point.y - height / 2],
   *   [point.x + width / 2, point.y + height / 2]
   * ], { layers: ['my-layer-name'] });
   * ```
   *
   * @example
   * Query all rendered features from a single layer
   * ```ts
   * let features = map.queryRenderedFeatures({ layers: ['my-layer-name'] });
   * ```
   * @see [Get features under the mouse pointer](https://maplibre.org/maplibre-gl-js/docs/examples/queryrenderedfeatures/)
   */
  fun queryRenderedFeatures(geometryOrOptions: Point = definedExternally, options: QueryRenderedFeaturesOptions = definedExternally): Array<MapGeoJSONFeature>

  fun queryRenderedFeatures(geometryOrOptions: Array<Point> = definedExternally, options: QueryRenderedFeaturesOptions = definedExternally): Array<MapGeoJSONFeature>

  fun queryRenderedFeatures(geometryOrOptions: QueryRenderedFeaturesOptions = definedExternally, options: QueryRenderedFeaturesOptions = definedExternally): Array<MapGeoJSONFeature>

  /**
   * Returns an array of MapGeoJSONFeature objects
   * representing features within the specified vector tile or GeoJSON source that satisfy the query parameters.
   *
   * @param sourceId - The ID of the vector tile or GeoJSON source to query.
   * @param parameters - The options object.
   * @returns An array of MapGeoJSONFeature objects.
   *
   * In contrast to {@link Map#queryRenderedFeatures}, this function returns all features matching the query parameters,
   * whether they are rendered by the current style (i.e. visible). The domain of the query includes all currently-loaded
   * vector tiles and GeoJSON source tiles: this function does not check tiles outside the currently
   * visible viewport.
   *
   * Because features come from tiled vector data or GeoJSON data that is converted to tiles internally, feature
   * geometries may be split or duplicated across tile boundaries and, as a result, features may appear multiple
   * times in query results. For example, suppose there is a highway running through the bounding rectangle of a query.
   * The results of the query will be those parts of the highway that lie within the map tiles covering the bounding
   * rectangle, even if the highway extends into other tiles, and the portion of the highway within each map tile
   * will be returned as a separate feature. Similarly, a point feature near a tile boundary may appear in multiple
   * tiles due to tile buffering.
   *
   * @example
   * Find all features in one source layer in a vector source
   * ```ts
   * let features = map.querySourceFeatures('your-source-id', {
   *   sourceLayer: 'your-source-layer'
   * });
   * ```
   *
   */
  fun querySourceFeatures(sourceId: String, parameters: QuerySourceFeatureOptions? = definedExternally): Array<MapGeoJSONFeature>

  /**
   * Updates the map's MapLibre style object with a new value.
   *
   * If a style is already set when this is used and options.diff is set to true, the map renderer will attempt to compare the given style
   * against the map's current state and perform only the changes necessary to make the map style match the desired state. Changes in sprites
   * (images used for icons and patterns) and glyphs (fonts for label text) **cannot** be diffed. If the sprites or fonts used in the current
   * style and the given style are different in any way, the map renderer will force a full update, removing the current style and building
   * the given one from scratch.
   *
   *
   * @param style - A JSON object conforming to the schema described in the
   * [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/), or a URL to such JSON.
   * @param options - The options object.
   * @returns `this`
   *
   * @example
   * ```ts
   * map.setStyle("https://demotiles.maplibre.org/style.json");
   *
   * map.setStyle('https://demotiles.maplibre.org/style.json', {
   *   transformStyle: (previousStyle, nextStyle) => ({
   *       ...nextStyle,
   *       sources: {
   *           ...nextStyle.sources,
   *           // copy a source from previous style
   *           'osm': previousStyle.sources.osm
   *       },
   *       layers: [
   *           // background layer
   *           nextStyle.layers[0],
   *           // copy a layer from previous style
   *           previousStyle.layers[0],
   *           // other layers from the next style
   *           ...nextStyle.layers.slice(1).map(layer => {
   *               // hide the layers we don't need from demotiles style
   *               if (layer.id.startsWith('geolines')) {
   *                   layer.layout = {...layer.layout || {}, visibility: 'none'};
   *               // filter out US polygons
   *               } else if (layer.id.startsWith('coastline') || layer.id.startsWith('countries')) {
   *                   layer.filter = ['!=', ['get', 'ADM0_A3'], 'USA'];
   *               }
   *               return layer;
   *           })
   *       ]
   *   })
   * });
   * ```
   */
  // fun setStyle(style: StyleSpecification?, options: Map$1SetStyleOptions = definedExternally): Map

  // fun setStyle(style: String?, options: Map$1SetStyleOptions = definedExternally): Map
  /**
   *  Updates the requestManager's transform request with a new function
   *
   * @param transformRequest - A callback run before the Map makes a request for an external URL. The callback can be used to modify the url, set headers, or set the credentials property for cross-origin requests.
   * Expected to return an object with a `url` property and optionally `headers` and `credentials` properties
   *
   * @returns `this`
   *
   * @example
   * ```ts
   * map.setTransformRequest((url: string, resourceType: string) => {});
   * ```
   */
  fun setTransformRequest(transformRequest: (url: String, resourceType: ResourceType?) -> RequestParameters?): Map
  // fun _getUIString(key: /* keyof typeof defaultLocale */): String
  // fun _updateStyle(style: StyleSpecification?, options: Map$1_updateStyleOptions = definedExternally): Map

  // fun _updateStyle(style: String?, options: Map$1_updateStyleOptions = definedExternally): Map
  fun _lazyInitEmptyStyle(): Unit
  // fun _diffStyle(style: StyleSpecification, options: Map$1_diffStyleOptions = definedExternally): Unit

  // fun _diffStyle(style: String, options: Map$1_diffStyleOptions = definedExternally): Unit
  // fun _updateDiff(style: StyleSpecification, options: Map$1_updateDiffOptions = definedExternally): Unit
  /**
   * Returns the map's MapLibre style object, a JSON object which can be used to recreate the map's style.
   *
   * @returns The map's style JSON object.
   *
   * @example
   * ```ts
   * let styleJson = map.getStyle();
   * ```
   *
   */
  // fun getStyle(): StyleSpecification
  /**
   * Returns a Boolean indicating whether the map's style is fully loaded.
   *
   * @returns A Boolean indicating whether the style is fully loaded.
   *
   * @example
   * ```ts
   * let styleLoadStatus = map.isStyleLoaded();
   * ```
   */
  fun isStyleLoaded(): Boolean
  /**
   * Adds a source to the map's style.
   *
   * Events triggered:
   *
   * Triggers the `source.add` event.
   *
   * @param id - The ID of the source to add. Must not conflict with existing sources.
   * @param source - The source object, conforming to the
   * MapLibre Style Specification's [source definition](https://maplibre.org/maplibre-style-spec/sources) or
   * {@link CanvasSourceSpecification}.
   * @returns `this`
   * @example
   * ```ts
   * map.addSource('my-data', {
   *   type: 'vector',
   *   url: 'https://demotiles.maplibre.org/tiles/tiles.json'
   * });
   * ```
   * @example
   * ```ts
   * map.addSource('my-data', {
   *   "type": "geojson",
   *   "data": {
   *     "type": "Feature",
   *     "geometry": {
   *       "type": "Point",
   *       "coordinates": [-77.0323, 38.9131]
   *     },
   *     "properties": {
   *       "title": "Mapbox DC",
   *       "marker-symbol": "monument"
   *     }
   *   }
   * });
   * ```
   * @see GeoJSON source: [Add live realtime data](https://maplibre.org/maplibre-gl-js/docs/examples/live-geojson/)
   */
  fun addSource(id: String, source: SourceSpecification): Map
  fun addSource(id: String, source: CanvasSourceSpecification): Map
  /**
   * Returns a Boolean indicating whether the source is loaded. Returns `true` if the source with
   * the given ID in the map's style has no outstanding network requests, otherwise `false`.
   *
   * A {@link ErrorEvent} event will be fired if there is no source wit the specified ID.
   *
   * @param id - The ID of the source to be checked.
   * @returns A Boolean indicating whether the source is loaded.
   * @example
   * ```ts
   * let sourceLoaded = map.isSourceLoaded('bathymetry-data');
   * ```
   */
  fun isSourceLoaded(id: String): Boolean
  /**
   * Loads a 3D terrain mesh, based on a "raster-dem" source.
   *
   * Triggers the `terrain` event.
   *
   * @param options - Options object.
   * @returns `this`
   * @example
   * ```ts
   * map.setTerrain({ source: 'terrain' });
   * ```
   */
  // fun setTerrain(options: TerrainSpecification?): Map
  /**
   * Get the terrain-options if terrain is loaded
   * @returns the TerrainSpecification passed to setTerrain
   * @example
   * ```ts
   * map.getTerrain(); // { source: 'terrain' };
   * ```
   */
  // fun getTerrain(): TerrainSpecification?
  /**
   * Returns a Boolean indicating whether all tiles in the viewport from all sources on
   * the style are loaded.
   *
   * @returns A Boolean indicating whether all tiles are loaded.
   * @example
   * ```ts
   * let tilesLoaded = map.areTilesLoaded();
   * ```
   */
  fun areTilesLoaded(): Boolean

  /**
   * Removes a source from the map's style.
   *
   * @param id - The ID of the source to remove.
   * @returns `this`
   * @example
   * ```ts
   * map.removeSource('bathymetry-data');
   * ```
   */
  fun removeSource(id: String): Map

  /**
   * Returns the source with the specified ID in the map's style.
   *
   * This method is often used to update a source using the instance members for the relevant
   * source type as defined in [Sources](#sources).
   * For example, setting the `data` for a GeoJSON source or updating the `url` and `coordinates`
   * of an image source.
   *
   * @param id - The ID of the source to get.
   * @returns The style source with the specified ID or `undefined` if the ID
   * corresponds to no existing sources.
   * The shape of the object varies by source type.
   * A list of options for each source type is available on the MapLibre Style Specification's
   * [Sources](https://maplibre.org/maplibre-style-spec/sources/) page.
   * @example
   * ```ts
   * let sourceObject = map.getSource('points');
   * ```
   * @see [Create a draggable point](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-point/)
   * @see [Animate a point](https://maplibre.org/maplibre-gl-js/docs/examples/animate-point-along-line/)
   * @see [Add live realtime data](https://maplibre.org/maplibre-gl-js/docs/examples/live-geojson/)
   */
  fun getSource(id: String): Source?
  /**
   * Add an image to the style. This image can be displayed on the map like any other icon in the style's
   * sprite using the image's ID with
   * [`icon-image`](https://maplibre.org/maplibre-style-spec/layers/#layout-symbol-icon-image),
   * [`background-pattern`](https://maplibre.org/maplibre-style-spec/layers/#paint-background-background-pattern),
   * [`fill-pattern`](https://maplibre.org/maplibre-style-spec/layers/#paint-fill-fill-pattern),
   * or [`line-pattern`](https://maplibre.org/maplibre-style-spec/layers/#paint-line-line-pattern).
   *
   * A {@link ErrorEvent} event will be fired if the image parameter is invalid or there is not enough space in the sprite to add this image.
   *
   * @param id - The ID of the image.
   * @param image - The image as an `HTMLImageElement`, `ImageData`, `ImageBitmap` or object with `width`, `height`, and `data`
   * properties with the same format as `ImageData`.
   * @param options - Options object.
   * @returns `this`
   * @example
   * ```ts
   * // If the style's sprite does not already contain an image with ID 'cat',
   * // add the image 'cat-icon.png' to the style's sprite with the ID 'cat'.
   * map.loadImage('https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Cat_silhouette.svg/400px-Cat_silhouette.svg.png', function(error, image) {
   *    if (error) throw error;
   *    if (!map.hasImage('cat')) map.addImage('cat', image);
   * });
   *
   * // Add a stretchable image that can be used with `icon-text-fit`
   * // In this example, the image is 600px wide by 400px high.
   * map.loadImage('https://upload.wikimedia.org/wikipedia/commons/8/89/Black_and_White_Boxed_%28bordered%29.png', function(error, image) {
   *    if (error) throw error;
   *    if (!map.hasImage('border-image')) {
   *      map.addImage('border-image', image, {
   *          content: [16, 16, 300, 384], // place text over left half of image, avoiding the 16px border
   *          stretchX: [[16, 584]], // stretch everything horizontally except the 16px border
   *          stretchY: [[16, 384]], // stretch everything vertically except the 16px border
   *      });
   *    }
   * });
   * ```
   * @see Use `HTMLImageElement`: [Add an icon to the map](https://maplibre.org/maplibre-gl-js/docs/examples/add-image/)
   * @see Use `ImageData`: [Add a generated icon to the map](https://maplibre.org/maplibre-gl-js/docs/examples/add-image-generated/)
   */
  // fun addImage(id: String, image: HTMLImageElement, options: Partial<StyleImageMetadata> = definedExternally): Map

  // fun addImage(id: String, image: ImageBitmap, options: Partial<StyleImageMetadata> = definedExternally): Map

  // fun addImage(id: String, image: ImageData, options: Partial<StyleImageMetadata> = definedExternally): Map

  // fun addImage(id: String, image: StyleImageInterface, options: Partial<StyleImageMetadata> = definedExternally): Map
  /**
   * Update an existing image in a style. This image can be displayed on the map like any other icon in the style's
   * sprite using the image's ID with
   * [`icon-image`](https://maplibre.org/maplibre-style-spec/layers/#layout-symbol-icon-image),
   * [`background-pattern`](https://maplibre.org/maplibre-style-spec/layers/#paint-background-background-pattern),
   * [`fill-pattern`](https://maplibre.org/maplibre-style-spec/layers/#paint-fill-fill-pattern),
   * or [`line-pattern`](https://maplibre.org/maplibre-style-spec/layers/#paint-line-line-pattern).
   *
   * An {@link ErrorEvent} will be fired if the image parameter is invald.
   *
   * @param id - The ID of the image.
   * @param image - The image as an `HTMLImageElement`, `ImageData`, `ImageBitmap` or object with `width`, `height`, and `data`
   * properties with the same format as `ImageData`.
   * @returns `this`
   * @example
   * ```ts
   * // If an image with the ID 'cat' already exists in the style's sprite,
   * // replace that image with a new image, 'other-cat-icon.png'.
   * if (map.hasImage('cat')) map.updateImage('cat', './other-cat-icon.png');
   * ```
   */
  fun updateImage(id: String, image: HTMLImageElement): Map

  fun updateImage(id: String, image: ImageBitmap): Map

  fun updateImage(id: String, image: ImageData): Map

  // fun updateImage(id: String, image: Temp43): Map

  fun updateImage(id: String, image: StyleImageInterface): Map
  /**
   * Returns an image, specified by ID, currently available in the map.
   * This includes both images from the style's original sprite
   * and any images that have been added at runtime using {@link Map#addImage}.
   *
   * @param id - The ID of the image.
   * @returns An image in the map with the specified ID.
   *
   * @example
   * ```ts
   * let coffeeShopIcon = map.getImage("coffee_cup");
   * ```
   */
  // fun getImage(id: String): StyleImage
  /**
   * Check whether an image with a specific ID exists in the style. This checks both images
   * in the style's original sprite and any images
   * that have been added at runtime using {@link Map#addImage}.
   *
   * An {@link ErrorEvent} will be fired if the image parameter is invald.
   *
   * @param id - The ID of the image.
   *
   * @returns A Boolean indicating whether the image exists.
   * @example
   * Check if an image with the ID 'cat' exists in the style's sprite.
   * ```ts
   * let catIconExists = map.hasImage('cat');
   * ```
   */
  fun hasImage(id: String): Boolean

  /**
   * Remove an image from a style. This can be an image from the style's original
   * sprite or any images
   * that have been added at runtime using {@link Map#addImage}.
   *
   * @param id - The ID of the image.
   *
   * @example
   * ```ts
   * // If an image with the ID 'cat' exists in
   * // the style's sprite, remove it.
   * if (map.hasImage('cat')) map.removeImage('cat');
   * ```
   */
  fun removeImage(id: String): Unit
  /**
   * Load an image from an external URL to be used with {@link Map#addImage}. External
   * domains must support [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS).
   *
   * @param url - The URL of the image file. Image file must be in png, webp, or jpg format.
   * @returns a promise that is resolved when the image is loaded
   *
   * @example
   * Load an image from an external URL.
   * ```ts
   * const response = await map.loadImage('http://placekitten.com/50/50');
   * // Add the loaded image to the style's sprite with the ID 'kitten'.
   * map.addImage('kitten', response.data);
   * ```
   * @see [Add an icon to the map](https://maplibre.org/maplibre-gl-js/docs/examples/add-image/)
   */
  // fun loadImage(url: String): Promise<GetResourceResponse<Any? /* HTMLImageElement | ImageBitmap */>>
  /**
   * Returns an Array of strings containing the IDs of all images currently available in the map.
   * This includes both images from the style's original sprite
   * and any images that have been added at runtime using {@link Map#addImage}.
   *
   * @returns An Array of strings containing the names of all sprites/images currently available in the map.
   *
   * @example
   * ```ts
   * let allImages = map.listImages();
   * ```
   */
  fun listImages(): Array<String>

  /**
   * Adds a [MapLibre style layer](https://maplibre.org/maplibre-style-spec/layers)
   * to the map's style.
   *
   * A layer defines how data from a specified source will be styled. Read more about layer types
   * and available paint and layout properties in the [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/layers).
   *
   * @param layer - The layer to add,
   * conforming to either the MapLibre Style Specification's [layer definition](https://maplibre.org/maplibre-style-spec/layers) or,
   * less commonly, the {@link CustomLayerInterface} specification. Can also be a layer definition with an embedded source definition.
   * The MapLibre Style Specification's layer definition is appropriate for most layers.
   *
   * @param beforeId - The ID of an existing layer to insert the new layer before,
   * resulting in the new layer appearing visually beneath the existing layer.
   * If this argument is not specified, the layer will be appended to the end of the layers array
   * and appear visually above all other layers.
   *
   * @returns `this`
   *
   * @example
   * Add a circle layer with a vector source
   * ```ts
   * map.addLayer({
   *   id: 'points-of-interest',
   *   source: {
   *     type: 'vector',
   *     url: 'https://demotiles.maplibre.org/tiles/tiles.json'
   *   },
   *   'source-layer': 'poi_label',
   *   type: 'circle',
   *   paint: {
   *     // MapLibre Style Specification paint properties
   *   },
   *   layout: {
   *     // MapLibre Style Specification layout properties
   *   }
   * });
   * ```
   *
   * @example
   * Define a source before using it to create a new layer
   * ```ts
   * map.addSource('state-data', {
   *   type: 'geojson',
   *   data: 'path/to/data.geojson'
   * });
   *
   * map.addLayer({
   *   id: 'states',
   *   // References the GeoJSON source defined above
   *   // and does not require a `source-layer`
   *   source: 'state-data',
   *   type: 'symbol',
   *   layout: {
   *     // Set the label content to the
   *     // feature's `name` property
   *     text-field: ['get', 'name']
   *   }
   * });
   * ```
   *
   * @example
   * Add a new symbol layer before an existing layer
   * ```ts
   * map.addLayer({
   *   id: 'states',
   *   // References a source that's already been defined
   *   source: 'state-data',
   *   type: 'symbol',
   *   layout: {
   *     // Set the label content to the
   *     // feature's `name` property
   *     text-field: ['get', 'name']
   *   }
   * // Add the layer before the existing `cities` layer
   * }, 'cities');
   * ```
   * @see [Create and style clusters](https://maplibre.org/maplibre-gl-js/docs/examples/cluster/)
   * @see [Add a vector tile source](https://maplibre.org/maplibre-gl-js/docs/examples/vector-source/)
   * @see [Add a WMS source](https://maplibre.org/maplibre-gl-js/docs/examples/wms/)
   */
  fun addLayer(layer: Any, beforeId: String = definedExternally): Map

  /**
   * Specifies a layer to be added to a {@link Style}. In addition to a standard {@link LayerSpecification}
   * or a {@link CustomLayerInterface}, a {@link LayerSpecification} with an embedded {@link SourceSpecification} can also be provided.
   */
//  typealias AddLayerObject = Any /* LayerSpecification
//    | (Omit<LayerSpecification, "source"> & { source: SourceSpecification; })
//    | CustomLayerInterface */



  /**
   * Moves a layer to a different z-position.
   *
   * @param id - The ID of the layer to move.
   * @param beforeId - The ID of an existing layer to insert the new layer before. When viewing the map, the `id` layer will appear beneath the `beforeId` layer. If `beforeId` is omitted, the layer will be appended to the end of the layers array and appear above all other layers on the map.
   * @returns `this`
   *
   * @example
   * Move a layer with ID 'polygon' before the layer with ID 'country-label'. The `polygon` layer will appear beneath the `country-label` layer on the map.
   * ```ts
   * map.moveLayer('polygon', 'country-label');
   * ```
   */
  fun moveLayer(id: String, beforeId: String = definedExternally): Map

  /**
   * Removes the layer with the given ID from the map's style.
   *
   * An {@link ErrorEvent} will be fired if the image parameter is invald.
   *
   * @param id - The ID of the layer to remove
   * @returns `this`
   *
   * @example
   * If a layer with ID 'state-data' exists, remove it.
   * ```ts
   * if (map.getLayer('state-data')) map.removeLayer('state-data');
   * ```
   */
  fun removeLayer(id: String): Map
  /**
   * Returns the layer with the specified ID in the map's style.
   *
   * @param id - The ID of the layer to get.
   * @returns The layer with the specified ID, or `undefined`
   * if the ID corresponds to no existing layers.
   *
   * @example
   * ```ts
   * let stateDataLayer = map.getLayer('state-data');
   * ```
   * @see [Filter symbols by toggling a list](https://maplibre.org/maplibre-gl-js/docs/examples/filter-markers/)
   * @see [Filter symbols by text input](https://maplibre.org/maplibre-gl-js/docs/examples/filter-markers-by-input/)
   */
  // fun getLayer(id: String): StyleLayer?
  /**
   * Return the ids of all layers currently in the style, including custom layers, in order.
   *
   * @returns ids of layers, in order
   *
   * @example
   * ```ts
   * const orderedLayerIds = map.getLayersOrder();
   * ```
   */
  fun getLayersOrder(): Array<String>

  /**
   * Sets the zoom extent for the specified style layer. The zoom extent includes the
   * [minimum zoom level](https://maplibre.org/maplibre-style-spec/layers/#minzoom)
   * and [maximum zoom level](https://maplibre.org/maplibre-style-spec/layers/#maxzoom))
   * at which the layer will be rendered.
   *
   * Note: For style layers using vector sources, style layers cannot be rendered at zoom levels lower than the
   * minimum zoom level of the _source layer_ because the data does not exist at those zoom levels. If the minimum
   * zoom level of the source layer is higher than the minimum zoom level defined in the style layer, the style
   * layer will not be rendered at all zoom levels in the zoom range.
   *
   * @param layerId - The ID of the layer to which the zoom extent will be applied.
   * @param minzoom - The minimum zoom to set (0-24).
   * @param maxzoom - The maximum zoom to set (0-24).
   * @returns `this`
   *
   * @example
   * ```ts
   * map.setLayerZoomRange('my-layer', 2, 5);
   * ```
   */
  fun setLayerZoomRange(layerId: String, minzoom: Double, maxzoom: Double): Map
  /**
   * Sets the filter for the specified style layer.
   *
   * Filters control which features a style layer renders from its source.
   * Any feature for which the filter expression evaluates to `true` will be
   * rendered on the map. Those that are false will be hidden.
   *
   * Use `setFilter` to show a subset of your source data.
   *
   * To clear the filter, pass `null` or `undefined` as the second parameter.
   *
   * @param layerId - The ID of the layer to which the filter will be applied.
   * @param filter - The filter, conforming to the MapLibre Style Specification's
   * [filter definition](https://maplibre.org/maplibre-style-spec/layers/#filter).  If `null` or `undefined` is provided, the function removes any existing filter from the layer.
   * @param options - Options object.
   * @returns `this`
   *
   * @example
   * Display only features with the 'name' property 'USA'
   * ```ts
   * map.setFilter('my-layer', ['==', ['get', 'name'], 'USA']);
   * ```
   * @example
   * Display only features with five or more 'available-spots'
   * ```ts
   * map.setFilter('bike-docks', ['>=', ['get', 'available-spots'], 5]);
   * ```
   * @example
   * Remove the filter for the 'bike-docks' style layer
   * ```ts
   * map.setFilter('bike-docks', null);
   * ```
   * @see [Create a timeline animation](https://maplibre.org/maplibre-gl-js/docs/examples/timeline-animation/)
   */
  // fun setFilter(layerId: String, filter: FilterSpecification? = definedExternally, options: StyleSetterOptions = definedExternally): Map
  /**
   * Returns the filter applied to the specified style layer.
   *
   * @param layerId - The ID of the style layer whose filter to get.
   * @returns The layer's filter.
   */
  // fun getFilter(layerId: String): FilterSpecification?

  /**
   * Sets the value of a paint property in the specified style layer.
   *
   * @param layerId - The ID of the layer to set the paint property in.
   * @param name - The name of the paint property to set.
   * @param value - The value of the paint property to set.
   * Must be of a type appropriate for the property, as defined in the [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/).
   * Pass `null` to unset the existing value.
   * @param options - Options object.
   * @returns `this`
   * @example
   * ```ts
   * map.setPaintProperty('my-layer', 'fill-color', '#faafee');
   * ```
   * @see [Change a layer's color with buttons](https://maplibre.org/maplibre-gl-js/docs/examples/color-switcher/)
   * @see [Create a draggable point](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-point/)
   */
  fun setPaintProperty(
    layerId: String,
    name: String,
    value: Any?,
    options: StyleSetterOptions = definedExternally
  ): Map

  /**
   * Returns the value of a paint property in the specified style layer.
   *
   * @param layerId - The ID of the layer to get the paint property from.
   * @param name - The name of a paint property to get.
   * @returns The value of the specified paint property.
   */
  fun getPaintProperty(layerId: String, name: String): Any?

  /**
   * Sets the value of a layout property in the specified style layer.
   *
   * @param layerId - The ID of the layer to set the layout property in.
   * @param name - The name of the layout property to set.
   * @param value - The value of the layout property. Must be of a type appropriate for the property, as defined in the [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/).
   * @param options - The options object.
   * @returns `this`
   * @example
   * ```ts
   * map.setLayoutProperty('my-layer', 'visibility', 'none');
   * ```
   */
  fun setLayoutProperty(
    layerId: String,
    name: String,
    value: Any?,
    options: StyleSetterOptions = definedExternally
  ): Map

  /**
   * Returns the value of a layout property in the specified style layer.
   *
   * @param layerId - The ID of the layer to get the layout property from.
   * @param name - The name of the layout property to get.
   * @returns The value of the specified layout property.
   */
  fun getLayoutProperty(layerId: String, name: String): Any?

  /**
   * Sets the value of the style's glyphs property.
   *
   * @param glyphsUrl - Glyph URL to set. Must conform to the [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/glyphs/).
   * @param options - Options object.
   * @returns `this`
   * @example
   * ```ts
   * map.setGlyphs('https://demotiles.maplibre.org/font/{fontstack}/{range}.pbf');
   * ```
   */
  fun setGlyphs(glyphsUrl: String?, options: StyleSetterOptions = definedExternally): Map

  /**
   * Returns the value of the style's glyphs URL
   *
   * @returns glyphs Style's glyphs url
   */
  fun getGlyphs(): String?

  /**
   * Adds a sprite to the map's style. Fires the `style` event.
   *
   * @param id - The ID of the sprite to add. Must not conflict with existing sprites.
   * @param url - The URL to load the sprite from
   * @param options - Options object.
   * @returns `this`
   * @example
   * ```ts
   * map.addSprite('sprite-two', 'http://example.com/sprite-two');
   * ```
   */
  fun addSprite(id: String, url: String, options: StyleSetterOptions = definedExternally): Map

  /**
   * Removes the sprite from the map's style. Fires the `style` event.
   *
   * @param id - The ID of the sprite to remove. If the sprite is declared as a single URL, the ID must be "default".
   * @returns `this`
   * @example
   * ```ts
   * map.removeSprite('sprite-two');
   * map.removeSprite('default');
   * ```
   */
  fun removeSprite(id: String): Map

  /**
   * Returns the as-is value of the style's sprite.
   *
   * @returns style's sprite list of id-url pairs
   */
  fun getSprite(): Array<IdUrl>

  /**
   * Sets the value of the style's sprite property.
   *
   * @param spriteUrl - Sprite URL to set.
   * @param options - Options object.
   * @returns `this`
   * @example
   * ```ts
   * map.setSprite('YOUR_SPRITE_URL');
   * ```
   */
  fun setSprite(spriteUrl: String?, options: StyleSetterOptions = definedExternally): Map
  /**
   * Sets the any combination of light values.
   *
   * @param light - Light properties to set. Must conform to the [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/light).
   * @param options - Options object.
   * @returns `this`
   *
   * @example
   * ```ts
   * let layerVisibility = map.getLayoutProperty('my-layer', 'visibility');
   * ```
   */
  // fun setLight(light: LightSpecification, options: StyleSetterOptions = definedExternally): Map
  /**
   * Returns the value of the light object.
   *
   * @returns light Light properties of the style.
   */
  // fun getLight(): LightSpecification
  /**
   * Sets the `state` of a feature.
   * A feature's `state` is a set of user-defined key-value pairs that are assigned to a feature at runtime.
   * When using this method, the `state` object is merged with any existing key-value pairs in the feature's state.
   * Features are identified by their `feature.id` attribute, which can be any number or string.
   *
   * This method can only be used with sources that have a `feature.id` attribute. The `feature.id` attribute can be defined in three ways:
   *
   * - For vector or GeoJSON sources, including an `id` attribute in the original data file.
   * - For vector or GeoJSON sources, using the [`promoteId`](https://maplibre.org/maplibre-style-spec/sources/#vector-promoteId) option at the time the source is defined.
   * - For GeoJSON sources, using the [`generateId`](https://maplibre.org/maplibre-style-spec/sources/#geojson-generateId) option to auto-assign an `id` based on the feature's index in the source data. If you change feature data using `map.getSource('some id').setData(..)`, you may need to re-apply state taking into account updated `id` values.
   *
   * _Note: You can use the [`feature-state` expression](https://maplibre.org/maplibre-style-spec/expressions/#feature-state) to access the values in a feature's state object for the purposes of styling._
   *
   * @param feature - Feature identifier. Feature objects returned from
   * {@link Map#queryRenderedFeatures} or event handlers can be used as feature identifiers.
   * @param state - A set of key-value pairs. The values should be valid JSON types.
   * @returns `this`
   *
   * @example
   * ```ts
   * // When the mouse moves over the `my-layer` layer, update
   * // the feature state for the feature under the mouse
   * map.on('mousemove', 'my-layer', function(e) {
   *   if (e.features.length > 0) {
   *     map.setFeatureState({
   *       source: 'my-source',
   *       sourceLayer: 'my-source-layer',
   *       id: e.features[0].id,
   *     }, {
   *       hover: true
   *     });
   *   }
   * });
   * ```
   * @see [Create a hover effect](https://maplibre.org/maplibre-gl-js/docs/examples/hover-styles/)
   */
  fun setFeatureState(feature: FeatureIdentifier, state: Any?): Map

  /**
   * Removes the `state` of a feature, setting it back to the default behavior.
   * If only a `target.source` is specified, it will remove the state for all features from that source.
   * If `target.id` is also specified, it will remove all keys for that feature's state.
   * If `key` is also specified, it removes only that key from that feature's state.
   * Features are identified by their `feature.id` attribute, which can be any number or string.
   *
   * @param target - Identifier of where to remove state. It can be a source, a feature, or a specific key of feature.
   * Feature objects returned from {@link Map#queryRenderedFeatures} or event handlers can be used as feature identifiers.
   * @param key - (optional) The key in the feature state to reset.
   * @returns `this`
   * @example
   * Reset the entire state object for all features in the `my-source` source
   * ```ts
   * map.removeFeatureState({
   *   source: 'my-source'
   * });
   * ```
   *
   * @example
   * When the mouse leaves the `my-layer` layer,
   * reset the entire state object for the
   * feature under the mouse
   * ```ts
   * map.on('mouseleave', 'my-layer', function(e) {
   *   map.removeFeatureState({
   *     source: 'my-source',
   *     sourceLayer: 'my-source-layer',
   *     id: e.features[0].id
   *   });
   * });
   * ```
   *
   * @example
   * When the mouse leaves the `my-layer` layer,
   * reset only the `hover` key-value pair in the
   * state for the feature under the mouse
   * ```ts
   * map.on('mouseleave', 'my-layer', function(e) {
   *   map.removeFeatureState({
   *     source: 'my-source',
   *     sourceLayer: 'my-source-layer',
   *     id: e.features[0].id
   *   }, 'hover');
   * });
   * ```
   */
  fun removeFeatureState(target: FeatureIdentifier, key: String = definedExternally): Map

  /**
   * Gets the `state` of a feature.
   * A feature's `state` is a set of user-defined key-value pairs that are assigned to a feature at runtime.
   * Features are identified by their `feature.id` attribute, which can be any number or string.
   *
   * _Note: To access the values in a feature's state object for the purposes of styling the feature, use the [`feature-state` expression](https://maplibre.org/maplibre-style-spec/expressions/#feature-state)._
   *
   * @param feature - Feature identifier. Feature objects returned from
   * {@link Map#queryRenderedFeatures} or event handlers can be used as feature identifiers.
   * @returns The state of the feature: a set of key-value pairs that was assigned to the feature at runtime.
   *
   * @example
   * When the mouse moves over the `my-layer` layer,
   * get the feature state for the feature under the mouse
   * ```ts
   * map.on('mousemove', 'my-layer', function(e) {
   *   if (e.features.length > 0) {
   *     map.getFeatureState({
   *       source: 'my-source',
   *       sourceLayer: 'my-source-layer',
   *       id: e.features[0].id
   *     });
   *   }
   * });
   * ```
   */
  fun getFeatureState(feature: FeatureIdentifier): Any?

  /**
   * Returns the map's containing HTML element.
   *
   * @returns The map's container.
   */
  fun getContainer(): HTMLElement

  /**
   * Returns the HTML element containing the map's `<canvas>` element.
   *
   * If you want to add non-GL overlays to the map, you should append them to this element.
   *
   * This is the element to which event bindings for map interactivity (such as panning and zooming) are
   * attached. It will receive bubbled events from child elements such as the `<canvas>`, but not from
   * map controls.
   *
   * @returns The container of the map's `<canvas>`.
   * @see [Create a draggable point](https://maplibre.org/maplibre-gl-js/docs/examples/drag-a-point/)
   */
  fun getCanvasContainer(): HTMLElement

  /**
   * Returns the map's `<canvas>` element.
   *
   * @returns The map's `<canvas>` element.
   * @see [Measure distances](https://maplibre.org/maplibre-gl-js/docs/examples/measure/)
   * @see [Display a popup on hover](https://maplibre.org/maplibre-gl-js/docs/examples/popup-on-hover/)
   * @see [Center the map on a clicked symbol](https://maplibre.org/maplibre-gl-js/docs/examples/center-on-symbol/)
   */
  fun getCanvas(): HTMLCanvasElement
  fun _containerDimensions(): Array<Double>
  fun _setupContainer(): Unit
  fun _resizeCanvas(width: Double, height: Double, pixelRatio: Double): Unit
  fun _setupPainter(): Unit
  var _contextLost: (event: Any?) -> Unit
  var _contextRestored: (event: Any?) -> Unit
  var _onMapScroll: (event: Any?) -> Boolean

  /**
   * Returns a Boolean indicating whether the map is fully loaded.
   *
   * Returns `false` if the style is not yet fully loaded,
   * or if there has been a change to the sources or style that
   * has not yet fully loaded.
   *
   * @returns A Boolean indicating whether the map is fully loaded.
   */
  fun loaded(): Boolean

  /**
   * @internal
   * Update this map's style and sources, and re-render the map.
   *
   * @param updateStyle - mark the map's style for reprocessing as
   * well as its sources
   * @returns `this`
   */
  fun _update(updateStyle: Boolean = definedExternally): Map

  /**
   * @internal
   * Request that the given callback be executed during the next render
   * frame.  Schedule a render frame if one is not already scheduled.
   *
   * @returns An id that can be used to cancel the callback
   */
//  fun _requestRenderFrame(callback: () -> Unit): TaskID
//  fun _cancelRenderFrame(id: TaskID): Unit
  fun _requestRenderFrame(callback: () -> Unit): Double
  fun _cancelRenderFrame(id: Double): Unit

  /**
   * @internal
   * Call when a (re-)render of the map is required:
   *
   * - The style has changed (`setPaintProperty()`, etc.)
   * - Source data has changed (e.g. tiles have finished loading)
   * - The map has is moving (or just finished moving)
   * - A transition is in progress
   *
   * @param paintStartTimeStamp - The time when the animation frame began executing.
   *
   * @returns `this`
   */
  fun _render(paintStartTimeStamp: Double): Map

  /**
   * Force a synchronous redraw of the map.
   * @returns `this`
   * @example
   * ```ts
   * map.redraw();
   * ```
   */
  fun redraw(): Map

  /**
   * Clean up and release all internal resources associated with this map.
   *
   * This includes DOM elements, event bindings, web workers, and WebGL resources.
   *
   * Use this method when you are done using the map and wish to ensure that it no
   * longer consumes browser resources. Afterwards, you must not call any other
   * methods on the map.
   */
  fun remove()

  /**
   * Trigger the rendering of a single frame. Use this method with custom layers to
   * repaint the map when the layer changes. Calling this multiple times before the
   * next frame is rendered will still result in only a single frame being rendered.
   * @example
   * ```ts
   * map.triggerRepaint();
   * ```
   * @see [Add a 3D model](https://maplibre.org/maplibre-gl-js/docs/examples/add-3d-model/)
   * @see [Add an animated icon to the map](https://maplibre.org/maplibre-gl-js/docs/examples/add-image-animated/)
   */
  fun triggerRepaint(): Unit
  var _onWindowOnline: () -> Unit

  /**
   * Gets and sets a Boolean indicating whether the map will render an outline
   * around each tile and the tile ID. These tile boundaries are useful for
   * debugging.
   *
   * The uncompressed file size of the first vector source is drawn in the top left
   * corner of each tile, next to the tile ID.
   *
   * @example
   * ```ts
   * map.showTileBoundaries = true;
   * ```
   */

  var showTileBoundaries: Boolean

  /**
   * Gets and sets a Boolean indicating whether the map will visualize
   * the padding offsets.
   */

  var showPadding: Boolean

  /**
   * Gets and sets a Boolean indicating whether the map will render boxes
   * around all symbols in the data source, revealing which symbols
   * were rendered or which were hidden due to collisions.
   * This information is useful for debugging.
   */

  var showCollisionBoxes: Boolean

  /**
   * Gets and sets a Boolean indicating whether the map should color-code
   * each fragment to show how many times it has been shaded.
   * White fragments have been shaded 8 or more times.
   * Black fragments have been shaded 0 times.
   * This information is useful for debugging.
   */

  var showOverdrawInspector: Boolean

  /**
   * Gets and sets a Boolean indicating whether the map will
   * continuously repaint. This information is useful for analyzing performance.
   */

  var repaint: Boolean


  var vertices: Boolean

  /**
   * Returns the package version of the library
   * @returns Package version of the library
   */

  val version: String

  /**
   * Returns the elevation for the point where the camera is looking.
   * This value corresponds to:
   * "meters above sea level" * "exaggeration"
   * @returns The elevation.
   */
  fun getCameraTargetElevation(): Double
}


/**
 * This function is used to transform a request.
 * It is used just before executing the relevant request.
 */
// typealias RequestTransformFunction = (url: String, resourceType: ResourceType? /* use undefined for default */) -> RequestParameters?


external class RequestManager(transformRequestFn: (url: String, resourceType: ResourceType?) -> RequestParameters? = definedExternally) {
  var _transformRequestFn: (url: String, resourceType: ResourceType?) -> RequestParameters?
  fun transformRequest(url: String, type: ResourceType): RequestParameters
  fun normalizeSpriteURL(url: String, format: String, extension: String): String
  fun setTransformRequest(transformRequest: (url: String, resourceType: ResourceType?) -> RequestParameters?): Unit
}

/**
 * A callback hook that allows manipulating the camera and being notified about camera updates before they happen
 */
//typealias CameraUpdateTransformFunction = (next: CameraUpdateTransformFunctionNext) -> CameraUpdateTransformFunctionNext

external interface CameraUpdateTransformFunctionNext {
  var center: LngLat
  var zoom: Double
  var pitch: Double
  var bearing: Double
  var elevation: Double
}

/**
 * A position definition for the control to be placed, can be in one of the corners of the map.
 * When two or more controls are places in the same location they are stacked toward the center of the map.
 */

// typealias ControlPosition = String
//@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
//@seskar.js.JsVirtual
//sealed external interface ControlPosition {
//  companion object {
//    @seskar.js.JsValue("top-left")
//    val topLeft: ControlPosition
//    @seskar.js.JsValue("top-right")
//    val topRight: ControlPosition
//    @seskar.js.JsValue("bottom-left")
//    val bottomLeft: ControlPosition
//    @seskar.js.JsValue("bottom-right")
//    val bottomRight: ControlPosition
//  }
//}

/**
 * The {@link AttributionControl} options object
 */
external interface AttributionControlOptions {
  /**
   * If `true`, the attribution control will always collapse when moving the map. If `false`,
   * force the expanded attribution control. The default is a responsive attribution that collapses when the user moves the map on maps less than 640 pixels wide.
   * **Attribution should not be collapsed if it can comfortably fit on the map. `compact` should only be used to modify default attribution when map size makes it impossible to fit default attribution and when the automatic compact resizing for default settings are not sufficient.**
   */
  var compact: Boolean?

  /**
   * Attributions to show in addition to any other attributions.
   */
  var customAttribution: (Any /* string | Array<string> */)?
}

external interface DelegatedListenerResult {
  var layer: String
  var listener: (a: Any?) -> Any?
  // var delegates: Temp41
}

/**
 * Options common to map movement methods that involve animation, such as {@link Map#panBy} and
 * {@link Map#easeTo}, controlling the duration and easing function of the animation. All properties
 * are optional.
 *
 */
external interface AnimationOptions {
  /**
   * The animation's duration, measured in milliseconds.
   */
  var duration: Double?

  /**
   * A function taking a time in the range 0..1 and returning a number where 0 is
   * the initial state and 1 is the final state.
   */
  var easing: ((_: Double) -> Double)?

  /**
   * of the target center relative to real map container center at the end of animation.
   */
  var offset: Point

  /**
   * If `false`, no animation will occur.
   */
  var animate: Boolean?

  /**
   * If `true`, then the animation is considered essential and will not be affected by
   * [`prefers-reduced-motion`](https://developer.mozilla.org/en-US/docs/Web/CSS/\@media/prefers-reduced-motion).
   */
  var essential: Boolean?

  /**
   * Default false. Needed in 3D maps to let the camera stay in a constant
   * height based on sea-level. After the animation finished the zoom-level will be recalculated in respect of
   * the distance from the camera to the center-coordinate-altitude.
   */
  var freezeElevation: Boolean?
}

/**
 * A listener method used as a callback to events
 */
// typealias Listener = (a: Any?) -> Any?

external interface Listeners {

  // @seskar.js.JsNative
  operator fun get(key: String): Array<(a: Any?) -> Any?>?

  // @seskar.js.JsNative
  operator fun set(key: String, value: Array<(a: Any?) -> Any?>?)

}

external class Transform(
  minZoom: Double = definedExternally,
  maxZoom: Double = definedExternally,
  minPitch: Double = definedExternally,
  maxPitch: Double = definedExternally,
  renderWorldCopies: Boolean = definedExternally
) {
  var tileSize: Double
  var tileZoom: Double
  var lngRange: Array<Double>   // length 2
  var latRange: Array<Double>   // length 2
  var maxValidLatitude: Double
  var scale: Double
  var width: Double
  var height: Double
  var angle: Double

  // var rotationMatrix: mat2
  var pixelsToGLUnits: Array<Double>  // length 2
  var cameraToCenterDistance: Double

  // var mercatorMatrix: mat4
  // var projMatrix: mat4
  // var invProjMatrix: mat4
  // var alignedProjMatrix: mat4
  // var pixelMatrix: mat4
  // var pixelMatrix3D: mat4
  // var pixelMatrixInverse: mat4
  // var glCoordMatrix: mat4
  // var labelPlaneMatrix: mat4
  var minElevationForCurrentTile: Double
  var _fov: Double
  var _pitch: Double
  var _zoom: Double
  var _unmodified: Boolean
  var _renderWorldCopies: Boolean
  var _minZoom: Double
  var _maxZoom: Double
  var _minPitch: Double
  var _maxPitch: Double
  var _center: LngLat
  var _elevation: Double
  var _pixelPerMeter: Double
  var _edgeInsets: EdgeInsets
  var _constraining: Boolean

  // var _posMatrixCache: Transform_posMatrixCache
  // var _alignedPosMatrixCache: Transform_alignedPosMatrixCache
  fun clone(): Transform
  fun apply(that: Transform): Unit

  var minZoom: Double


  var maxZoom: Double


  var minPitch: Double


  var maxPitch: Double


  var renderWorldCopies: Boolean


  val worldSize: Double


  val centerOffset: Point


  val size: Point


  var bearing: Double


  var pitch: Double


  var fov: Double


  var zoom: Double


  var center: LngLat


  var elevation: Double


  var padding: PaddingOptions

  /**
   * The center of the screen in pixels with the top-left corner being (0,0)
   * and +y axis pointing downwards. This accounts for padding.
   */

  val centerPoint: Point

  /**
   * Returns if the padding params match
   *
   * @param padding - the padding to check against
   * @returns true if they are equal, false otherwise
   */
  fun isPaddingEqual(padding: PaddingOptions): Boolean

  /**
   * Helper method to update edge-insets in place
   *
   * @param start - the starting padding
   * @param target - the target padding
   * @param t - the step/weight
   */
  fun interpolatePadding(start: PaddingOptions, target: PaddingOptions, t: Double): Unit

  /**
   * Return a zoom level that will cover all tiles the transform
   * @param options - the options
   * @returns zoom level An integer zoom level at which all tiles will be visible.
   */
  fun coveringZoomLevel(options: TransformCoveringZoomLevelOptions): Double

  /**
   * Return any "wrapped" copies of a given tile coordinate that are visible
   * in the current view.
   */
  // fun getVisibleUnwrappedCoordinates(tileID: CanonicalTileID): Array<UnwrappedTileID>

  /**
   * Return all coordinates that could cover this transform for a covering
   * zoom level.
   * @param options - the options
   * @returns OverscaledTileIDs
   */
  fun coveringTiles(options: TransformCoveringTilesOptions): Array<OverscaledTileID>
  fun resize(width: Double, height: Double): Unit

  val unmodified: Boolean

  fun zoomScale(zoom: Double): Double
  fun scaleZoom(scale: Double): Double

  /**
   * Convert from LngLat to world coordinates (Mercator coordinates scaled by 512)
   * @param lnglat - the lngLat
   * @returns Point
   */
  fun project(lnglat: LngLat): Point

  /**
   * Convert from world coordinates ([0, 512],[0, 512]) to LngLat ([-180, 180], [-90, 90])
   * @param point - world coordinate
   * @returns LngLat
   */
  fun unproject(point: Point): LngLat

  val point: Point

  /**
   * get the camera position in LngLat and altitudes in meter
   * @returns An object with lngLat & altitude.
   */
  fun getCameraPosition(): TransformGetCameraPositionResult

  /**
   * This method works in combination with freezeElevation activated.
   * freezeElevtion is enabled during map-panning because during this the camera should sit in constant height.
   * After panning finished, call this method to recalculate the zoomlevel for the current camera-height in current terrain.
   * @param terrain - the terrain
   */
  // fun recalculateZoom(terrain: Terrain): Unit
  fun setLocationAtPoint(lnglat: LngLat, point: Point): Unit

  /**
   * Given a LngLat location, return the screen point that corresponds to it
   * @param lnglat - location
   * @param terrain - optional terrain
   * @returns screen point
   */
  // fun locationPoint(lnglat: LngLat, terrain: Terrain = definedExternally): Point

  /**
   * Given a point on screen, return its lnglat
   * @param p - screen point
   * @param terrain - optional terrain
   * @returns lnglat location
   */
  // fun pointLocation(p: Point, terrain: Terrain = definedExternally): LngLat

  /**
   * Given a geographical lnglat, return an unrounded
   * coordinate that represents it at low zoom level.
   * @param lnglat - the location
   * @returns The mercator coordinate
   */
  // fun locationCoordinate(lnglat: LngLat): MercatorCoordinate

  /**
   * Given a Coordinate, return its geographical position.
   * @param coord - mercator coordinates
   * @returns lng and lat
   */
  // fun coordinateLocation(coord: MercatorCoordinate): LngLat

  /**
   * Given a Point, return its mercator coordinate.
   * @param p - the point
   * @param terrain - optional terrain
   * @returns lnglat
   */
  // fun pointCoordinate(p: Point, terrain: Terrain = definedExternally): MercatorCoordinate

  /**
   * Given a coordinate, return the screen point that corresponds to it
   * @param coord - the coordinates
   * @param elevation - the elevation
   * @param pixelMatrix - the pixel matrix
   * @returns screen point
   */
  fun coordinatePoint(
    // coord: MercatorCoordinate,
    elevation: Double = definedExternally,
    // pixelMatrix: mat4 = definedExternally
  ): Point

  /**
   * Returns the map's geographical bounds. When the bearing or pitch is non-zero, the visible region is not
   * an axis-aligned rectangle, and the result is the smallest bounds that encompasses the visible region.
   * @returns Returns a {@link LngLatBounds} object describing the map's geographical bounds.
   */
  fun getBounds(): LngLatBounds

  /**
   * Returns the maximum geographical bounds the map is constrained to, or `null` if none set.
   * @returns max bounds
   */
  fun getMaxBounds(): LngLatBounds?

  /**
   * Calculate pixel height of the visible horizon in relation to map-center (e.g. height/2),
   * multiplied by a static factor to simulate the earth-radius.
   * The calculated value is the horizontal line from the camera-height to sea-level.
   * @returns Horizon above center in pixels.
   */
  fun getHorizon(): Double

  /**
   * Sets or clears the map's geographical constraints.
   * @param bounds - A {@link LngLatBounds} object describing the new geographic boundaries of the map.
   */
  fun setMaxBounds(bounds: LngLatBounds? = definedExternally): Unit

  /**
   * Calculate the posMatrix that, given a tile coordinate, would be used to display the tile on a map.
   * @param unwrappedTileID - the tile ID
   */
  // fun calculatePosMatrix(unwrappedTileID: UnwrappedTileID, aligned: Boolean = definedExternally): mat4
  // fun customLayerMatrix(): mat4
  fun _constrain(): Unit
  fun _calcMatrices(): Unit
  fun maxPitchScaleFactor(): Double

  /**
   * The camera looks at the map from a 3D (lng, lat, altitude) location. Let's use `cameraLocation`
   * as the name for the location under the camera and on the surface of the earth (lng, lat, 0).
   * `cameraPoint` is the projected position of the `cameraLocation`.
   *
   * This point is useful to us because only fill-extrusions that are between `cameraPoint` and
   * the query point on the surface of the earth can extend and intersect the query.
   *
   * When the map is not pitched the `cameraPoint` is equivalent to the center of the map because
   * the camera is right above the center of the map.
   */
  fun getCameraPoint(): Point

  /**
   * When the map is pitched, some of the 3D features that intersect a query will not intersect
   * the query at the surface of the earth. Instead the feature may be closer and only intersect
   * the query because it extrudes into the air.
   * @param queryGeometry - For point queries, the line from the query point to the "camera point",
   * for other geometries, the envelope of the query geometry and the "camera point"
   * @returns a geometry that includes all of the original query as well as all possible ares of the
   * screen where the *base* of a visible extrusion could be.
   *
   */
  fun getCameraQueryGeometry(queryGeometry: Array<Point>): Array<Point>

  /**
   * Return the distance to the camera in clip space from a LngLat.
   * This can be compared to the value from the depth buffer (terrain.depthAtPoint)
   * to determine whether a point is occluded.
   * @param lngLat - the point
   * @param elevation - the point's elevation
   * @returns depth value in clip space (between 0 and 1)
   */
  fun lngLatToCameraDepth(lngLat: LngLat, elevation: Double): Double
}

external interface TransformGetCameraPositionResult {
  var lngLat: LngLat
  var altitude: Double
}

/**
 * Options common to {@link Map#jumpTo}, {@link Map#easeTo}, and {@link Map#flyTo}, controlling the desired location,
 * zoom, bearing, and pitch of the camera. All properties are optional, and when a property is omitted, the current
 * camera value for that property will remain unchanged.
 *
 * @example
 * Set the map's initial perspective with CameraOptions
 * ```ts
 * let map = new Map({
 *   container: 'map',
 *   style: 'https://demotiles.maplibre.org/style.json',
 *   center: [-73.5804, 45.53483],
 *   pitch: 60,
 *   bearing: -60,
 *   zoom: 10
 * });
 * ```
 * @see [Set pitch and bearing](https://maplibre.org/maplibre-gl-js/docs/examples/set-perspective/)
 * @see [Jump to a series of locations](https://maplibre.org/maplibre-gl-js/docs/examples/jump-to/)
 * @see [Fly to a location](https://maplibre.org/maplibre-gl-js/docs/examples/flyto/)
 * @see [Display buildings in 3D](https://maplibre.org/maplibre-gl-js/docs/examples/3d-buildings/)
 */
external interface CameraOptions : CenterZoomBearing {
  /**
   * The desired pitch in degrees. The pitch is the angle towards the horizon
   * measured in degrees with a range between 0 and 60 degrees. For example, pitch: 0 provides the appearance
   * of looking straight down at the map, while pitch: 60 tilts the user's perspective towards the horizon.
   * Increasing the pitch value is often used to display 3D objects.
   */
  var pitch: Double?

  /**
   * If `zoom` is specified, `around` determines the point around which the zoom is centered.
   */
  var around: LngLat?
}

/**
 * Holds center, zoom and bearing properties
 */
external interface CenterZoomBearing {
  /**
   * The desired center.
   */
  var center: LngLat?

  /**
   * The desired zoom level.
   */
  var zoom: Double?

  /**
   * The desired bearing in degrees. The bearing is the compass direction that
   * is "up". For example, `bearing: 90` orients the map so that east is up.
   */
  var bearing: Double?
}

// typealias TaskID = Double

/**
 * Options for setting padding on calls to methods such as {@link Map#fitBounds}, {@link Map#fitScreenCoordinates}, and {@link Map#setPadding}. Adjust these options to set the amount of padding in pixels added to the edges of the canvas. Set a uniform padding on all edges or individual values for each edge. All properties of this object must be
 * non-negative integers.
 *
 * @group Geography and Geometry
 *
 * @example
 * ```ts
 * let bbox = [[-79, 43], [-73, 45]];
 * map.fitBounds(bbox, {
 *   padding: {top: 10, bottom:25, left: 15, right: 5}
 * });
 * ```
 *
 * @example
 * ```ts
 * let bbox = [[-79, 43], [-73, 45]];
 * map.fitBounds(bbox, {
 *   padding: 20
 * });
 * ```
 * @see [Fit to the bounds of a LineString](https://maplibre.org/maplibre-gl-js/docs/examples/zoomto-linestring/)
 * @see [Fit a map to a bounding box](https://maplibre.org/maplibre-gl-js/docs/examples/fitbounds/)
 */
external interface PaddingOptions {
  /**
   * Padding in pixels from the top of the map canvas.
   */
  var top: Double

  /**
   * Padding in pixels from the bottom of the map canvas.
   */
  var bottom: Double

  /**
   * Padding in pixels from the left of the map canvas.
   */
  var right: Double

  /**
   * Padding in pixels from the right of the map canvas.
   */
  var left: Double
}

/**
 * A options object for the {@link Map#cameraForBounds} method
 */
external interface CameraForBoundsOptions : CameraOptions {
  /**
   * The amount of padding in pixels to add to the given bounds.
   */
  var padding: (Any /* number | RequireAtLeastOne<PaddingOptions> */)?

  /**
   * The center of the given bounds relative to the map's center, measured in pixels.
   * @defaultValue [0, 0]
   */
  var offset: Point

  /**
   * The maximum zoom level to allow when the camera would transition to the specified bounds.
   */
  var maxZoom: Double?
}

/**
 * Options for {@link Map#fitBounds} method
 */
external interface FitBoundsOptions : FlyToOptions {
  /**
   * If `true`, the map transitions using {@link Map#easeTo}. If `false`, the map transitions using {@link Map#flyTo}.
   * See those functions and {@link AnimationOptions} for information about options available.
   * @defaultValue false
   */
  var linear: Boolean?

  /**
   * The center of the given bounds relative to the map's center, measured in pixels.
   * @defaultValue [0, 0]
   */
  // var offset: Point

  /**
   * The maximum zoom level to allow when the map view transitions to the specified bounds.
   */
  var maxZoom: Double?
}

/**
 * The {@link Map#flyTo} options object
 */
external interface FlyToOptions : AnimationOptions, CameraOptions {
  /**
   * The zooming "curve" that will occur along the
   * flight path. A high value maximizes zooming for an exaggerated animation, while a low
   * value minimizes zooming for an effect closer to {@link Map#easeTo}. 1.42 is the average
   * value selected by participants in the user study discussed in
   * [van Wijk (2003)](https://www.win.tue.nl/~vanwijk/zoompan.pdf). A value of
   * `Math.pow(6, 0.25)` would be equivalent to the root mean squared average velocity. A
   * value of 1 would produce a circular motion.
   * @defaultValue 1.42
   */
  var curve: Double?

  /**
   * The zero-based zoom level at the peak of the flight path. If
   * `options.curve` is specified, this option is ignored.
   */
  var minZoom: Double?

  /**
   * The average speed of the animation defined in relation to
   * `options.curve`. A speed of 1.2 means that the map appears to move along the flight path
   * by 1.2 times `options.curve` screenfuls every second. A _screenful_ is the map's visible span.
   * It does not correspond to a fixed physical distance, but varies by zoom level.
   * @defaultValue 1.2
   */
  var speed: Double?

  /**
   * The average speed of the animation measured in screenfuls
   * per second, assuming a linear timing curve. If `options.speed` is specified, this option is ignored.
   */
  var screenSpeed: Double?

  /**
   * The animation's maximum duration, measured in milliseconds.
   * If duration exceeds maximum duration, it resets to 0.
   */
  var maxDuration: Double?

  /**
   * The amount of padding in pixels to add to the given bounds.
   */
  var padding: (Any /* number | RequireAtLeastOne<PaddingOptions> */)?
}

/**
 * The options object related to the {@link Map#jumpTo} method
 */
external interface JumpToOptions : CameraOptions {
  /**
   * Dimensions in pixels applied on each side of the viewport for shifting the vanishing point.
   */
  var padding: PaddingOptions?
}

external interface CameraEaseToOptions : EaseToOptions {
  var easeId: String?
  var noMoveStart: Boolean?
}

external interface EaseToOptions : AnimationOptions, CameraOptions {
  var delayEndEvents: Double?
  var padding: (Any /* number | RequireAtLeastOne<PaddingOptions> */)?
}

external interface Camera_easeOptions {
  var animate: Boolean?
  var duration: Double?
  var easing: ((_: Double) -> Double)?
}

external interface Task {
  var callback: (timeStamp: Double) -> Unit
  // var id: TaskID
  var id: Double
  var cancelled: Boolean
}

external class TaskQueue {
  constructor ()

  var _queue: Array<Task>
  // var _id: TaskID
  var _id: Double
  var _cleared: Boolean
  var _currentlyRunning: Any /* Array<Task> | false */
  // fun add(callback: (timeStamp: Double) -> Unit): TaskID
  fun add(callback: (timeStamp: Double) -> Unit): Double
  // fun remove(id: TaskID): Unit
  fun remove(id: Double): Unit
  fun run(timeStamp: Double = definedExternally): Unit
  fun clear(): Unit
}


/**
 * Interface for interactive controls added to the map. This is a
 * specification for implementers to model: it is not
 * an exported method or class.
 *
 * Controls must implement `onAdd` and `onRemove`, and must own an
 * element, which is often a `div` element. To use MapLibre GL JS's
 * default control styling, add the `maplibregl-ctrl` class to your control's
 * node.
 *
 * @example
 * Control implemented as ES6 class
 * ```ts
 * class HelloWorldControl {
 *     onAdd(map) {
 *         this._map = map;
 *         this._container = document.createElement('div');
 *         this._container.className = 'maplibregl-ctrl';
 *         this._container.textContent = 'Hello, world';
 *         return this._container;
 *     }
 *
 *     onRemove() {
 *         this._container.parentNode.removeChild(this._container);
 *         this._map = undefined;
 *     }
 * }
 *
 * // Control implemented as ES5 prototypical class
 * function HelloWorldControl() { }
 *
 * HelloWorldControl.prototype.onAdd = function(map) {
 *     this._map = map;
 *     this._container = document.createElement('div');
 *     this._container.className = 'maplibregl-ctrl';
 *     this._container.textContent = 'Hello, world';
 *     return this._container;
 * };
 *
 * HelloWorldControl.prototype.onRemove = function () {
 *      this._container.parentNode.removeChild(this._container);
 *      this._map = undefined;
 * };
 * ```
 */
external interface IControl {
  /**
   * Register a control on the map and give it a chance to register event listeners
   * and resources. This method is called by {@link Map#addControl}
   * internally.
   *
   * @param map - the Map this control will be added to
   * @returns The control's container element. This should
   * be created by the control and returned by onAdd without being attached
   * to the DOM: the map will insert the control's element into the DOM
   * as necessary.
   */
  fun onAdd(map: Map): HTMLElement

  /**
   * Unregister a control on the map and give it a chance to detach event listeners
   * and resources. This method is called by {@link Map#removeControl}
   * internally.
   *
   * @param map - the Map this control will be removed from
   */
  fun onRemove(map: Map): Unit

  /**
   * Optionally provide a default position for this control. If this method
   * is implemented and {@link Map#addControl} is called without the `position`
   * parameter, the value returned by getDefaultPosition will be used as the
   * control's position.
   *
   * @returns a control position, one of the values valid in addControl.
   */
  // val getDefaultPosition: (() -> ControlPosition)?
  val getDefaultPosition: (() -> String)?
}

/**
 * Options to pass to query the map for the rendered features
 */
external interface QueryRenderedFeaturesOptions {
  /**
   * An array of [style layer IDs](https://maplibre.org/maplibre-style-spec/#layer-id) for the query to inspect.
   * Only features within these layers will be returned. If this parameter is undefined, all layers will be checked.
   */
  var layers: Array<String>?
  /**
   * A [filter](https://maplibre.org/maplibre-style-spec/layers/#filter) to limit query results.
   */
  // var filter: FilterSpecification?
  /**
   * An array of string representing the available images
   */
  var availableImages: Array<String>?

  /**
   * Whether to check if the [options.filter] conforms to the MapLibre Style Specification. Disabling validation is a performance optimization that should only be used if you have previously validated the values you will be passing to this function.
   */
  var validate: Boolean?
}

/**
 * The options object related to the {@link Map#querySourceFeatures} method
 */
external interface QuerySourceFeatureOptions {
  /**
   * The name of the source layer to query. *For vector tile sources, this parameter is required.* For GeoJSON sources, it is ignored.
   */
  var sourceLayer: String?

  /**
   * A [filter](https://maplibre.org/maplibre-style-spec/layers/#filter)
   * to limit query results.
   */
  // var filter: FilterSpecification?

  /**
   * Whether to check if the [parameters.filter] conforms to the MapLibre Style Specification. Disabling validation is a performance optimization that should only be used if you have previously validated the values you will be passing to this function.
   * @defaultValue true
   */
  var validate: Boolean?
}

/**
 * The `Source` interface must be implemented by each source type, including "core" types (`vector`, `raster`,
 * `video`, etc.) and all custom, third-party types.
 *
 * @event `data` - Fired with `{dataType: 'source', sourceDataType: 'metadata'}` to indicate that any necessary metadata
 * has been loaded so that it's okay to call `loadTile`; and with `{dataType: 'source', sourceDataType: 'content'}`
 * to indicate that the source data has changed, so that any current caches should be flushed.
 *
 * @group Sources
 */
external interface Source {
  val type: String

  /**
   * The id for the source. Must not be used by any existing source.
   */
  var id: String

  /**
   * The minimum zoom level for the source.
   */
  var minzoom: Double

  /**
   * The maximum zoom level for the source.
   */
  var maxzoom: Double

  /**
   * The tile size for the source.
   */
  var tileSize: Double

  /**
   * The attribution for the source.
   */
  var attribution: String?

  /**
   * `true` if zoom levels are rounded to the nearest integer in the source data, `false` if they are floor-ed to the nearest integer.
   */
  var roundZoom: Boolean?

  /**
   * `false` if tiles can be drawn outside their boundaries, `true` if they cannot.
   */
  var isTileClipped: Boolean?
  var tileID: CanonicalTileID?

  /**
   * `true` if tiles should be sent back to the worker for each overzoomed zoom level, `false` if not.
   */
  var reparseOverscaled: Boolean?
  var vectorLayerIds: Array<String>?

  /**
   * True if the source has transiotion, false otherwise.
   */
  fun hasTransition(): Boolean

  /**
   * True if the source is loaded, false otherwise.
   */
  fun loaded(): Boolean

  /**
   * An ability to fire an event to all the listeners, see {@link Evented}
   * @param event - The event to fire
   */
  fun fire(event: Event): Any?

  /**
   * This method is called when the source is added to the map.
   * @param map - The map instance
   */
  fun onAdd(map: Map): Unit

  /**
   * This method is called when the source is removed from the map.
   * @param map - The map instance
   */
  fun onRemove(map: Map): Unit
  /**
   * This method does the heavy lifting of loading a tile.
   * In most cases it will defer the work to the relevant worker source.
   * @param tile - The tile to load
   */
  // fun loadTile(tile: Tile): Promise<Unit>
  /**
   * True is the tile is part of the source, false otherwise.
   * @param tileID - The tile ID
   */
  // fun hasTile(tileID: OverscaledTileID): Boolean
  /**
   * Allows to abort a tile loading.
   * @param tile - The tile to abort
   */
  // fun abortTile(tile: Tile): Promise<Unit>
  /**
   * Allows to unload a tile.
   * @param tile - The tile to unload
   */
  // fun unloadTile(tile: Tile): Promise<Unit>
  /**
   * @returns A plain (stringifiable) JS object representing the current state of the source.
   * Creating a source using the returned object as the `options` should result in a Source that is
   * equivalent to this one.
   */
  fun serialize(): Any

  /**
   * Allows to execute a prepare step before the source is used.
   */
  fun prepare(): Unit
}

/**
 * Interface for dynamically generated style images. This is a specification for
 * implementers to model: it is not an exported method or class.
 *
 * Images implementing this interface can be redrawn for every frame. They can be used to animate
 * icons and patterns or make them respond to user input. Style images can implement a
 * {@link StyleImageInterface#render} method. The method is called every frame and
 * can be used to update the image.
 *
 * @see [Add an animated icon to the map.](https://maplibre.org/maplibre-gl-js/docs/examples/add-image-animated/)
 *
 * @example
 * ```ts
 * let flashingSquare = {
 *     width: 64,
 *     height: 64,
 *     data: new Uint8Array(64 * 64 * 4),
 *
 *     onAdd: function(map) {
 *         this.map = map;
 *     },
 *
 *     render: function() {
 *         // keep repainting while the icon is on the map
 *         this.map.triggerRepaint();
 *
 *         // alternate between black and white based on the time
 *         let value = Math.round(Date.now() / 1000) % 2 === 0  ? 255 : 0;
 *
 *         // check if image needs to be changed
 *         if (value !== this.previousValue) {
 *             this.previousValue = value;
 *
 *             let bytesPerPixel = 4;
 *             for (let x = 0; x < this.width; x++) {
 *                 for (let y = 0; y < this.height; y++) {
 *                     let offset = (y * this.width + x) * bytesPerPixel;
 *                     this.data[offset + 0] = value;
 *                     this.data[offset + 1] = value;
 *                     this.data[offset + 2] = value;
 *                     this.data[offset + 3] = 255;
 *                 }
 *             }
 *
 *             // return true to indicate that the image changed
 *             return true;
 *         }
 *     }
 *  }
 *
 *  map.addImage('flashing_square', flashingSquare);
 * ```
 */
external interface StyleImageInterface {
  var width: Double
  var height: Double
  var data: Any /* Uint8Array | Uint8ClampedArray */

  /**
   * This method is called once before every frame where the icon will be used.
   * The method can optionally update the image's `data` member with a new image.
   *
   * If the method updates the image it must return `true` to commit the change.
   * If the method returns `false` or nothing the image is assumed to not have changed.
   *
   * If updates are infrequent it maybe easier to use {@link Map#updateImage} to update
   * the image instead of implementing this method.
   *
   * @returns `true` if this method updated the image. `false` if the image was not changed.
   */
  var render: (() -> Boolean)?

  /**
   * Optional method called when the layer has been added to the Map with {@link Map#addImage}.
   *
   * @param map - The Map this custom layer was just added to.
   */
  var onAdd: ((map: Map, id: String) -> Unit)?

  /**
   * Optional method called when the icon is removed from the map with {@link Map#removeImage}.
   * This gives the image a chance to clean up resources and event listeners.
   */
  var onRemove: (() -> Unit)?
}

/**
 * Supporting type to add validation to another style related type
 */
external interface StyleSetterOptions {
  /**
   * Whether to check if the filter conforms to the MapLibre Style Specification. Disabling validation is a performance optimization that should only be used if you have previously validated the values you will be passing to this function.
   */
  var validate: Boolean?
}

external interface IdUrl {
  var id: String
  var url: String
}

/**
 * A feature identifier that is bound to a source
 */
external interface FeatureIdentifier {
  /**
   * Unique id of the feature.
   */
  var id: Any? /* string | number | undefined */

  /**
   * The id of the vector or GeoJSON source for the feature.
   */
  var source: String

  /**
   * *For vector tile sources, `sourceLayer` is required.*
   */
  var sourceLayer: String?
}

/**
 * A type of MapLibre resource.
 */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface ResourceType {
  companion object {
    val Glyphs: ResourceType
    val Image: ResourceType
    val Source: ResourceType
    val SpriteImage: ResourceType
    val SpriteJSON: ResourceType
    val Style: ResourceType
    val Tile: ResourceType
    val Unknown: ResourceType
  }
}

/**
 * A `RequestParameters` object to be returned from Map.options.transformRequest callbacks.
 * @example
 * ```ts
 * // use transformRequest to modify requests that begin with `http://myHost`
 * transformRequest: function(url, resourceType) {
 *  if (resourceType === 'Source' && url.indexOf('http://myHost') > -1) {
 *    return {
 *      url: url.replace('http', 'https'),
 *      headers: { 'my-custom-header': true },
 *      credentials: 'include'  // Include cookies for cross-origin requests
 *    }
 *   }
 * }
 * ```
 */
external interface RequestParameters {
  /**
   * The URL to be requested.
   */
  var url: String

  /**
   * The headers to be sent with the request.
   */
  var headers: Any?
  /**
   * Request method `'GET' | 'POST' | 'PUT'`.
   */
  // var method: (RequestParametersMethod)?
  /**
   * Request body.
   */
  var body: String?
  /**
   * Response body type to be returned.
   */
  // var type: (RequestParametersType)?
  /**
   * `'same-origin'|'include'` Use 'include' to send cookies with cross-origin requests.
   */
  // var credentials: (RequestParametersCredentials)?
  /**
   * If `true`, Resource Timing API information will be collected for these transformed requests and returned in a resourceTiming property of relevant data events.
   */
  var collectResourceTiming: Boolean?

  /**
   * Parameters supported only by browser fetch API. Property of the Request interface contains the cache mode of the request. It controls how the request will interact with the browser's HTTP cache. (https://developer.mozilla.org/en-US/docs/Web/API/Request/cache)
   */
  var cache: RequestCache
}


/**
 * An `EdgeInset` object represents screen space padding applied to the edges of the viewport.
 * This shifts the apprent center or the vanishing point of the map. This is useful for adding floating UI elements
 * on top of the map and having the vanishing point shift as UI elements resize.
 *
 * @group Geography and Geometry
 */
external class EdgeInsets {
  constructor (
    top: Double = definedExternally,
    bottom: Double = definedExternally,
    left: Double = definedExternally,
    right: Double = definedExternally
  )

  /**
   * @defaultValue 0
   */
  var top: Double

  /**
   * @defaultValue 0
   */
  var bottom: Double

  /**
   * @defaultValue 0
   */
  var left: Double

  /**
   * @defaultValue 0
   */
  var right: Double

  /**
   * Interpolates the inset in-place.
   * This maintains the current inset value for any inset not present in `target`.
   * @param start - interpolation start
   * @param target - interpolation target
   * @param t - interpolation step/weight
   * @returns the insets
   */
  fun interpolate(start: PaddingOptions, target: PaddingOptions, t: Double): EdgeInsets

  fun interpolate(start: EdgeInsets, target: PaddingOptions, t: Double): EdgeInsets

  /**
   * Utility method that computes the new apprent center or vanishing point after applying insets.
   * This is in pixels and with the top left being (0.0) and +y being downwards.
   *
   * @param width - the width
   * @param height - the height
   * @returns the point
   */
  fun getCenter(width: Double, height: Double): Point
  fun equals(other: PaddingOptions): Boolean
  fun clone(): EdgeInsets

  /**
   * Returns the current state as json, useful when you want to have a
   * read-only representation of the inset.
   *
   * @returns state as json
   */
  fun toJSON(): PaddingOptions
}

external interface TransformCoveringZoomLevelOptions {
  /**
   * Target zoom level. If true, the value will be rounded to the closest integer. Otherwise the value will be floored.
   */
  var roundZoom: Boolean?

  /**
   * Tile size, expressed in screen pixels.
   */
  var tileSize: Double
}

external interface TransformCoveringTilesOptions {
  var tileSize: Double
  var minzoom: Double
  var maxzoom: Double
  var roundZoom: Boolean
  var reparseOverscaled: Boolean?
  var renderWorldCopies: Boolean?
  // var terrain: Terrain?
}
