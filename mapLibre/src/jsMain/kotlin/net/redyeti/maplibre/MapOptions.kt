package net.redyeti.maplibre

import js.objects.jso
import net.redyeti.maplibre.jsobject.LngLat
import net.redyeti.maplibre.jsobject.LngLatBounds

data class MapOptions(
  /**
   * If `true`, the gl context will be created with MSAA antialiasing, which can be useful for antialiasing custom
   * layers. This is `false` by default as a performance optimization.
   */
  var antialias: Boolean = false,

  /**
   * If set, an {@link AttributionControl} will be added to the map with the provided options.
   * To disable the attribution control, pass `false`.
   * Note: showing the logo of MapLibre is not required for using MapLibre.
   * @defaultValue compact: true, customAttribution: "MapLibre ...".
   */
  var attributionControl: Any = false, /* false | AttributionControlOptions */

  /**
   * The initial bearing (rotation) of the map, measured in degrees counter-clockwise from north. If `bearing` is not specified in the constructor options, MapLibre GL JS will look for it in the map's style object. If it is not specified in the style, either, it will default to `0`.
   */
  var bearing: Double = 0.0,

  /**
   * The threshold, measured in degrees, that determines when the map's
   * bearing will snap to north. For example, with a `bearingSnap` of 7, if the user rotates
   * the map within 7 degrees of north, the map will automatically snap to exact north.
   */
  var bearingSnap: Double = 7.0,

  /**
   * The initial bounds of the map. If `bounds` is specified, it overrides `center` and `zoom` constructor options.
   */
  var bounds: LngLatBounds? = null,

  /**
   * If `true`, the "box zoom" interaction is enabled (see [BoxZoomHandler]).
   */
  var boxZoom: Boolean = true,

  /**
   * Determines whether to cancel, or retain, tiles from the current viewport which are still loading but which belong
   * to a farther (smaller) zoom level than the current one. * If true, when zooming in, tiles which didn't manage to
   * load for previous zoom levels will become canceled. This might save some computing resources for slower devices,
   * but the map details might appear more abruptly at the end of the zoom. * If false, when zooming in, the previous
   * zoom level(s) tiles will progressively appear, giving a smoother map details experience. However, more tiles will
   * be rendered in a short period of time.
   */
  var cancelPendingTileRequestsWhileZooming: Boolean = true,

  /**
   * The initial geographical centerpoint of the map. If `center` is not specified in the constructor options, MapLibre
   * GL JS will look for it in the map's style object. If it is not specified in the style, either, it will default to
   * `[0, 0]` Note: MapLibre GL JS uses longitude, latitude coordinate order (as opposed to latitude, longitude) to
   * match GeoJSON.
   */
  var center: LngLat? = null,

  /**
   * The max number of pixels a user can shift the mouse pointer during a click for it to be considered a valid click
   * (as opposed to a mouse drag).
   */
  var clickTolerance: Double = 3.0,

  /**
   * If `true`, Resource Timing API information will be collected for requests made by GeoJSON and Vector Tile web
   * workers (this information is normally inaccessible from the main Javascript thread). Information will be returned
   * in a `resourceTiming` property of relevant `data` events.
   */
  var collectResourceTiming: Boolean = false,

  /**
   * The HTML element in which MapLibre GL JS will render the map, or the element's string `id`. The specified element
   * must have no children.
   */
  var container: String,

  /**
   * The {@link CooperativeGesturesHandler} options object for the gesture settings. If `true` or set to an options
   * object, the map is only accessible on desktop while holding Command/Ctrl and only accessible on mobile with two
   * fingers. Interacting with the map using normal gestures will trigger an informational screen. With this option
   * enabled, "drag to pitch" requires a three-finger gesture. Cooperative gestures are disabled when a map enters
   * fullscreen using {@link FullscreenControl}.
   */
  var cooperativeGestures: Boolean = false,

  /**
   * If `true`, symbols from multiple sources can collide with each other during collision detection. If `false`,
   * collision detection is run separately for the symbols in each source.
   */
  var crossSourceCollisions: Boolean = true,

  /**
   * If `true`, the "double click to zoom" interaction is enabled (see {@link DoubleClickZoomHandler}).
   */
  var doubleClickZoom: Boolean = true,

  /**
   * If `true`, the "drag to pan" interaction is enabled. An `Object` value is passed as options to {@link DragPanHandler#enable}.
   * @defaultValue true
   */
//  var dragPan: (Any /* boolean | DragPanOptions */)?

  /**
   * If `true`, the "drag to rotate" interaction is enabled (see {@link DragRotateHandler}).
   */
  var dragRotate: Boolean = true,

  /**
   * Controls the duration of the fade-in/fade-out animation for label collisions after initial map load, in
   * milliseconds. This setting affects all symbol layers. This setting does not affect the duration of runtime
   * styling transitions or raster tile cross-fading.
   */
  var fadeDuration: Double = 300.0,

  /**
   * If `true`, map creation will fail if the performance of MapLibre GL JS would be dramatically worse than expected
   * (i.e. a software renderer would be used).
   */
  var failIfMajorPerformanceCaveat: Boolean = false,

  /**
   * A {@link FitBoundsOptions} options object to use _only_ when fitting the initial `bounds` provided above.
   */
  // var fitBoundsOptions: FitBoundsOptions?

  /**
   * If `true`, the map's position (zoom, center latitude, center longitude, bearing, and pitch) will be synced with
   * the hash fragment of the page's URL.
   * For example, `http://path/to/my/page.html#2.59/39.26/53.07/-24.1/60`.
   * An additional string may optionally be provided to indicate a parameter-styled hash,
   * e.g. http://path/to/my/page.html#map=2.59/39.26/53.07/-24.1/60&foo=bar, where foo
   * is a custom parameter and bar is an arbitrary hash distinct from the map hash.
   * @defaultValue false
   */
  var hash: Any = false, //v(Any /* boolean | string */)?

  /**
   * If `false`, no mouse, touch, or keyboard listeners will be attached to the map, so it will not respond to interaction.
   */
  var interactive: Boolean = true,

  /**
   * If `true`, keyboard shortcuts are enabled (see {@link KeyboardHandler}).
   */
  var keyboard: Boolean = true,

  /**
   *  Defines a CSS
   * font-family for locally overriding generation of glyphs in the 'CJK Unified Ideographs', 'Hiragana', 'Katakana'
   * and 'Hangul Syllables' ranges. In these ranges, font settings from the map's style will be ignored, except for
   * font-weight keywords (light/regular/medium/bold). Set to `false`, to enable font settings from the map's style for
   * these glyph ranges. The purpose of this option is to avoid bandwidth-intensive glyph server requests. (See
   * [Use locally generated ideographs](https://maplibre.org/maplibre-gl-js/docs/examples/local-ideographs).)
   */
  var localIdeographFontFamily: String = "sans-serif",

  /**
   * A patch to apply to the default localization table for UI strings, e.g. control tooltips. The `locale` object maps
   * namespaced UI string IDs to translated strings in the target language; see `src/ui/default_locale.js` for an
   * example with all supported string IDs. The object may specify all UI strings (thereby adding support for a new
   * translation) or only a subset of strings (thereby patching the default translation table).
   */
  var locale: Any? = null,

  /**
   * A string representing the position of the MapLibre wordmark on the map. Valid options are `top-left`,`top-right`,
   * `bottom-left`, or `bottom-right`.
   * @defaultValue 'bottom-left'
   */
//  var logoPosition: ControlPosition?

  /**
   * If `true`, the MapLibre logo will be shown.
   */
  var maplibreLogo: Boolean = false,

  /**
   * If set, the map will be constrained to the given bounds.
   */
  var maxBounds: LngLatBounds? = null,

  /**
   * The canvas' `width` and `height` max size. The values are passed as an array where the first element is max width
   * and the second element is max height. You shouldn't set this above WebGl `MAX_TEXTURE_SIZE`. Defaults to
   * [4096, 4096].
   */
//  var maxCanvasSize: Array<Int>?

  /**
   * The maximum pitch of the map (0-85). Values greater than 60 degrees are experimental and may result in rendering
   * issues. If you encounter any, please raise an issue with details in the MapLibre project.
   */
  var maxPitch: Double = 60.0,

  /**
   * The maximum number of tiles stored in the tile cache for a given source. If omitted, the cache will be dynamically
   * sized based on the current viewport which can be set using `maxTileCacheZoomLevels` constructor options.
   */
  var maxTileCacheSize: Double? = null,

  /**
   * The maximum number of zoom levels for which to store tiles for a given source. Tile cache dynamic size is
   * calculated by multiplying `maxTileCacheZoomLevels` with the approximate number of tiles in the viewport for a
   * given source.
   */
  var maxTileCacheZoomLevels: Double = 5.0,

  /**
   * The maximum zoom level of the map (0-24).
   */
  var maxZoom: Double = 22.0,

  /**
   * The minimum pitch of the map (0-85). Values greater than 60 degrees are experimental and may result in rendering
   * issues. If you encounter any, please raise an issue with details in the MapLibre project.
   */
  var minPitch: Double = 0.0,

  /**
   * The minimum zoom level of the map (0-24).
   */
  var minZoom: Double = 0.0,

  /**
   * The initial pitch (tilt) of the map, measured in degrees away from the plane of the screen (0-85). If `pitch` is
   * not specified in the constructor options, MapLibre GL JS will look for it in the map's style object. If it is not
   * specified in the style, either, it will default to `0`. Values greater than 60 degrees are experimental and may
   * result in rendering issues. If you encounter any, please raise an issue with details in the MapLibre project.
   */
  var pitch: Double = 0.0,

  /**
   * If `false`, the map's pitch (tilt) control with "drag to rotate" interaction will be disabled.
   */
  var pitchWithRotate: Boolean = true,

  /**
   * The pixel ratio. The canvas' `width` attribute will be `container.clientWidth * pixelRatio` and its `height`
   * attribute will be `container.clientHeight * pixelRatio`. Defaults to `devicePixelRatio` if not specified.
   */
  var pixelRatio: Double? = null,

  /**
   * If `true`, the map's canvas can be exported to a PNG using `map.getCanvas().toDataURL()`. This is `false` by
   * default as a performance optimization.
   */
  var preserveDrawingBuffer: Boolean = false,

  /**
   * If `false`, the map won't attempt to re-request tiles once they expire per their HTTP `cacheControl`/`expires`
   * headers.
   */
  var refreshExpiredTiles: Boolean = true,

  /**
   * If `true`, multiple copies of the world will be rendered side by side beyond -180 and 180 degrees longitude. If
   * set to `false`:
   *
   * - When the map is zoomed out far enough that a single representation of the world does not fill the map's entire
   * container, there will be blank space beyond 180 and -180 degrees longitude.
   * - Features that cross 180 and -180 degrees longitude will be cut in two (with one portion on the right edge of the
   * map and the other on the left edge of the map) at every zoom level.
   */
  var renderWorldCopies: Boolean = true,

  /**
   * If `true`, the "scroll to zoom" interaction is enabled. {@link AroundCenterOptions} are passed as options to
   * [ScrollZoomHandler#enable].
   * @defaultValue true
   */
//  var scrollZoom: (Any /* boolean | AroundCenterOptions */)?

  /**
   * The map's MapLibre style. This must be a JSON object conforming to
   * the schema described in the [MapLibre Style Specification](https://maplibre.org/maplibre-style-spec/),
   * or a URL to such JSON.
   */
  var style: String,

  /**
   * If `true`, the "drag to pitch" interaction is enabled. An `Object` value is passed as options to {@link TwoFingersTouchPitchHandler#enable}.
   * @defaultValue true
   */
//  var touchPitch: (Any /* boolean | AroundCenterOptions */)?

  /**
   * If `true`, the "pinch to rotate and zoom" interaction is enabled. An `Object` value is passed as options to {@link TwoFingersTouchZoomRotateHandler#enable}.
   * @defaultValue true
   */
//  var touchZoomRotate: (Any /* boolean | AroundCenterOptions */)?

  /**
   * If `true`, the map will automatically resize when the browser window resizes.
   */
  var trackResize: Boolean = true,

  /**
   * A callback run before the map's camera is moved due to user input or animation. The callback can be used to modify
   * the new center, zoom, pitch and bearing.
   * Expected to return an object containing center, zoom, pitch or bearing values to overwrite.
   */
  //var transformCameraUpdate: (next: CameraUpdateTransformFunctionNext) -> CameraUpdateTransformFunctionNext?,

  /**
   * A callback run before the Map makes a request for an external URL. The callback can be used to modify the url, set
   * headers, or set the credentials property for cross-origin requests. Expected to return an object with a `url`
   * property and optionally `headers` and `credentials` properties.
   */
  //var transformRequest: (next: CameraUpdateTransformFunctionNext) -> CameraUpdateTransformFunctionNext?,

  /**
   * If false, style validation will be skipped. Useful in production environment.
   */
  var validateStyle: Boolean = true,

  /**
   * The initial zoom level of the map. If `zoom` is not specified in the constructor options, MapLibre GL JS will look
   * for it in the map's style object. If it is not specified in the style, either, it will default to `0`.
   */
  var zoom: Double = 0.0,
) {
  fun toJsMapOptions(): net.redyeti.maplibre.jsobject.MapOptions {
    var opt = this
    return jso {
      antialias = opt.antialias
      attributionControl = opt.attributionControl
      bearing = opt.bearing
      bearingSnap = opt.bearingSnap
      bounds = opt.bounds
      boxZoom = opt.boxZoom
      cancelPendingTileRequestsWhileZooming = opt.cancelPendingTileRequestsWhileZooming
      opt.center?.let { opt.center = it }
      clickTolerance = opt.clickTolerance
      collectResourceTiming = opt.collectResourceTiming
      container = opt.container
      cooperativeGestures = opt.cooperativeGestures
      crossSourceCollisions = opt.crossSourceCollisions
      doubleClickZoom = opt.doubleClickZoom
      dragRotate = opt.dragRotate
      fadeDuration = opt.fadeDuration
      failIfMajorPerformanceCaveat = opt.failIfMajorPerformanceCaveat
      hash = opt.hash
      interactive = opt.interactive
      localIdeographFontFamily = opt.localIdeographFontFamily
      locale = opt.locale
      maplibreLogo = opt.maplibreLogo
      maxBounds = opt.maxBounds
      maxPitch = opt.maxPitch
      maxTileCacheSize = opt.maxTileCacheSize
      maxTileCacheZoomLevels = opt.maxTileCacheZoomLevels
      maxZoom = opt.maxZoom
      minPitch = opt.minPitch
      minZoom = opt.minZoom
      pitch = opt.pitch
      pitchWithRotate = opt.pitchWithRotate
      opt.pixelRatio?.let { pixelRatio = it }
      preserveDrawingBuffer = opt.preserveDrawingBuffer
      refreshExpiredTiles = opt.refreshExpiredTiles
      renderWorldCopies = opt.renderWorldCopies
      style = opt.style
      trackResize = opt.trackResize
      validateStyle = opt.validateStyle
      zoom = opt.zoom
    }
  }
}
