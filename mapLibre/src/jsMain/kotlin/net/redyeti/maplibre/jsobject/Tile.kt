package net.redyeti.maplibre.jsobject

import js.buffer.ArrayBuffer

/**
 * The tile's state, can be:
 *
 * - `loading` Tile data is in the process of loading.
 * - `loaded` Tile data has been loaded. Tile can be rendered.
 * - `reloading` Tile data has been loaded and is being updated. Tile can be rendered.
 * - `unloaded` Tile data has been deleted.
 * - `errored` Tile data was not loaded because of an error.
 * - `expired` Tile data was previously loaded, but has expired per its HTTP headers and is in the process of refreshing.
 */
// export type TileState = 'loading' | 'loaded' | 'reloading' | 'unloaded' | 'errored' | 'expired'

/**
 * A tile object is the combination of a Coordinate, which defines
 * its place, as well as a unique ID and data tracking for its content
 */
external class Tile {
  var tileID: OverscaledTileID
  var uid: Double
  var uses: Double
  var tileSize: Double
  //var buckets: {[_: string]: Bucket}
  //var latestFeatureIndex: FeatureIndex
  var latestRawTileData: ArrayBuffer
  //var imageAtlas: ImageAtlas
  //var imageAtlasTexture: Texture
  //var glyphAtlasImage: AlphaImage
  //var glyphAtlasTexture: Texture
  var expirationTime: Any
  var expiredRequestCount: Double
  //var state: TileState
  var timeAdded: Double
  var fadeEndTime: Double
  //var collisionBoxArray: CollisionBoxArray
  var redoWhenDone: Boolean
  var showCollisionBoxes: Boolean
  var placementSource: Any
  //var actor: Actor
  //var vtLayers: {[_: string]: VectorTileLayer}

  var neighboringTiles: Any
  //var dem: DEMData
  //var demMatrix: mat4
  var aborted: Boolean
  var needsHillshadePrepare: Boolean
  var needsTerrainPrepare: Boolean
  //var abortController: AbortController
  //var texture: any
  //var fbo: Framebuffer
  //var demTexture: Texture
  var refreshedUponExpiration: Boolean
  //var reloadPromise: {resolve: () => void; reject: () => void}
  //var resourceTiming: Array<PerformanceResourceTiming>
  var queryPadding: Double

  var symbolFadeHoldUntil: Double
  var hasSymbolBuckets: Boolean
  var hasRTLText: Boolean
  var dependencies: Any
  //var rtt: Array<{id: Double; stamp: Double}>
  //var rttCoords: {[_:String]: String}

  /**
   * @param tileID - the tile ID
   * @param size - The tile size
   */
  constructor(tileID: OverscaledTileID, size: Double)

  fun registerFadeDuration(duration: Double)
  
  fun wasRequested()
  
  fun clearTextures(painter: Any)

  /**
   * Given a data object with a 'buffers' property, load it into
   * this tile's elementGroups and buffers properties and set loaded
   * to true. If the data is null, like in the case of an empty
   * GeoJSON tile, no-op but still set loaded to true.
   * @param data - The data from the worker
   * @param painter - the painter
   * @param justReloaded - `true` to just reload
   */
  // fun loadVectorData(data: WorkerTileResult, painter: Any, justReloaded: Boolean?)

  /**
   * Release any data or WebGL resources referenced by this tile.
   */
  fun unloadVectorData()

  //fun prepare(imageManager: ImageManager)

  // Queries non-symbol features rendered for this tile.
  // Symbol features are queried globally
//  fun queryRenderedFeatures(
//  layers: {[_: String]: StyleLayer},
//  serializedLayers: {[_: String]: any},
//  sourceFeatureState: SourceFeatureState,
//  queryGeometry: Array<Point>,
//  cameraQueryGeometry: Array<Point>,
//  scale: Double,
//  params: {
//    filter: FilterSpecification
//    layers: Array<String>
//    availableImages: Array<String>
//  },
//  transform: IReadonlyTransform,
//  maxPitchScaleFactor: Double,
//  pixelPosMatrix: mat4
//  ): {[_: String]: Array<{featureIndex: Double; feature: GeoJSONFeature}>}

//  fun querySourceFeatures(result: Array<GeoJSONFeature>, params?: {
//    sourceLayer: String?
//    filter: FilterSpecification?
//    validate: Boolean?
//  })
  
  fun hasData(): Boolean

  fun patternsLoaded(): Boolean

  //fun setExpiryData(data: ExpiryData)

  fun getExpiryTimeout(): Int

  //fun setFeatureState(states: LayerFeatureStates, painter: Any)

  fun holdingForFade(): Boolean

  fun symbolFadeFinished(): Boolean

  fun clearFadeHold()

  fun setHoldDuration(duration: Double)

  fun setDependencies(namespace: String, dependencies: Array<String>)

  fun hasDependency(namespaces: Array<String>, keys: Array<String>): Boolean
}