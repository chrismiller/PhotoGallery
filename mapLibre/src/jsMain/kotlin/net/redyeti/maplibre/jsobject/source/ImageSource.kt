package net.redyeti.maplibre.jsobject.source

import net.redyeti.maplibre.jsobject.CanonicalTileID
import net.redyeti.maplibre.jsobject.Event
import net.redyeti.maplibre.jsobject.Evented
import net.redyeti.maplibre.jsobject.Point
import net.redyeti.maplibre.jsobject.Source
import org.w3c.dom.HTMLImageElement
import net.redyeti.maplibre.jsobject.Map
import net.redyeti.maplibre.jsobject.geo.MercatorCoordinate

external interface Coordinates // = [[number, number], [number, number], [number, number], [number, number]]

/**
 * The options object for the {@link ImageSource#updateImage} method
 */
external interface UpdateImageOptions {
  /**
   * Required image URL.
   */
  var url: String
  /**
   * The image coordinates
   */
  var coordinates: Coordinates?
}

/**
 * A data source containing an image.
 * (See the [Style Specification](https://maplibre.org/maplibre-style-spec/#sources-image) for detailed documentation of options.)
 *
 * @group Sources
 *
 * @example
 * ```ts
 * // add to map
 * map.addSource('some id', {
 *    type: 'image',
 *    url: 'https://www.maplibre.org/images/foo.png',
 *    coordinates: [
 *        [-76.54, 39.18],
 *        [-76.52, 39.18],
 *        [-76.52, 39.17],
 *        [-76.54, 39.17]
 *    ]
 * })
 *
 * // update coordinates
 * let mySource = map.getSource('some id')
 * mySource.setCoordinates([
 *     [-76.54335737228394, 39.18579907229748],
 *     [-76.52803659439087, 39.1838364847587],
 *     [-76.5295386314392, 39.17683392507606],
 *     [-76.54520273208618, 39.17876344106642]
 * ])
 *
 * // update url and coordinates simultaneously
 * mySource.updateImage({
 *    url: 'https://www.maplibre.org/images/bar.png',
 *    coordinates: [
 *        [-76.54335737228394, 39.18579907229748],
 *        [-76.52803659439087, 39.1838364847587],
 *        [-76.5295386314392, 39.17683392507606],
 *        [-76.54520273208618, 39.17876344106642]
 *    ]
 * })
 *
 * map.removeSource('some id');  // remove
 * ```
 */
open external class ImageSource: Evented, Source {
  var url: String

  //var coordinates: Coordinates
  //var tiles: {[_: String]: Tile}
  var options: Any
  //var dispatcher: Dispatcher
  var map: Map
  //var texture: Texture?
  //var image: HTMLImageElement | ImageBitmap
  override val type: String
  override var id: String
  override var minzoom: Double
  override var maxzoom: Double
  override var tileSize: Double
  override var attribution: String?
  override var roundZoom: Boolean?
  override var isTileClipped: Boolean?
  override var tileID: CanonicalTileID?
  override var reparseOverscaled: Boolean?
  override var vectorLayerIds: Array<String>?

  override fun hasTransition(): Boolean

  var tileCoords: Array<Point>
  var flippedWindingOrder: Boolean = definedExternally

  /** @internal */
  //constructor(id: String, options: ImageSourceSpecification | VideoSourceSpecification | CanvasSourceSpecification, dispatcher: Dispatcher, eventedParent: Evented) {

  //async load(newCoordinates?: Coordinates): Promise<void> {

  override fun loaded(): Boolean
  override fun fire(event: Event): Any?
  override fun onAdd(map: Map)
  override fun onRemove(map: Map)
  override fun serialize(): Any
  override fun prepare()

  /**
   * Updates the image URL and, optionally, the coordinates. To avoid having the image flash after changing,
   * set the `raster-fade-duration` paint property on the raster layer to 0.
   *
   * @param options - The options object.
   */
  fun updateImage(options: UpdateImageOptions): ImageSource

  fun onRemove()

  /**
   * Sets the image's coordinates and re-renders the map.
   *
   * @param coordinates - Four geographical coordinates,
   * represented as arrays of longitude and latitude numbers, which define the corners of the image.
   * The coordinates start at the top left corner of the image and proceed in clockwise order.
   * They do not have to represent a rectangle.
   */
  fun setCoordinates(coordinates: Coordinates): ImageSource

  //async loadTile(tile: Tile): Promise<void>

  //fun serialize(): ImageSourceSpecification | VideoSourceSpecification | CanvasSourceSpecification
}

/**
 * Given a list of coordinates, get their center as a coordinate.
 *
 * @returns centerpoint
 * @internal
 */
external fun getCoordinatesCenterTileID(coords: Array<MercatorCoordinate>): CanonicalTileID

external fun hasWrongWindingOrder(coords: Array<Point>): Boolean