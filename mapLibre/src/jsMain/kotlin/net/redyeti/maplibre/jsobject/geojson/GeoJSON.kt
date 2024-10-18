@file:JsModule("geojson")
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package net.redyeti.maplibre.jsobject.geojson

import kotlinx.js.JsPlainObject

// Note: as of the RFC 7946 version of GeoJSON, Coordinate Reference Systems
// are no longer supported. (See https://tools.ietf.org/html/rfc7946#appendix-B)}
/* export as namespace GeoJSON; */

/**
 * The valid values for the "type" property of GeoJSON geometry objects.
 * https://tools.ietf.org/html/rfc7946#section-1.4
 */
sealed external interface GeoJsonGeometryTypes {
  companion object {
    @seskar.js.JsValue("Point")
    val Point: GeoJsonGeometryTypes
    @seskar.js.JsValue("MultiPoint")
    val MultiPoint: GeoJsonGeometryTypes
    @seskar.js.JsValue("LineString")
    val LineString: GeoJsonGeometryTypes
    @seskar.js.JsValue("MultiLineString")
    val MultiLineString: GeoJsonGeometryTypes
    @seskar.js.JsValue("Polygon")
    val Polygon: GeoJsonGeometryTypes
    @seskar.js.JsValue("MultiPolygon")
    val MultiPolygon: GeoJsonGeometryTypes
    @seskar.js.JsValue("GeometryCollection")
    val GeometryCollection: GeoJsonGeometryTypes
  }
}

/**
 * The value values for the "type" property of GeoJSON Objects.
 * https://tools.ietf.org/html/rfc7946#section-1.4
 */
sealed external interface GeoJsonTypes {
  companion object {
    @seskar.js.JsValue("Point")
    val Point: GeoJsonTypes
    @seskar.js.JsValue("MultiPoint")
    val MultiPoint: GeoJsonTypes
    @seskar.js.JsValue("LineString")
    val LineString: GeoJsonTypes
    @seskar.js.JsValue("MultiLineString")
    val MultiLineString: GeoJsonTypes
    @seskar.js.JsValue("Polygon")
    val Polygon: GeoJsonTypes
    @seskar.js.JsValue("MultiPolygon")
    val MultiPolygon: GeoJsonTypes
    @seskar.js.JsValue("GeometryCollection")
    val GeometryCollection: GeoJsonTypes
    @seskar.js.JsValue("Feature")
    val Feature: GeoJsonTypes
    @seskar.js.JsValue("FeatureCollection")
    val FeatureCollection: GeoJsonTypes
  }
}

/**
 * Bounding box
 * https://tools.ietf.org/html/rfc7946#section-5
 */
typealias BBox = Array<Double> /* [
    number,
    number,
    number,
    number
] | [
    number,
    number,
    number,
    number,
    number,
    number
] */

/**
 * A Position is an array of coordinates.
 * https://tools.ietf.org/html/rfc7946#section-3.1.1
 * Array should contain between two and three elements.
 * The previous GeoJSON specification allowed more elements (e.g., which could be used to represent M values),
 * but the current specification only allows X, Y, and (optionally) Z to be defined.
 */
typealias Position = Array<Double>// [number, number] | [number, number, number];


/**
 * The base GeoJSON object.
 * https://tools.ietf.org/html/rfc7946#section-3
 * The GeoJSON specification also allows foreign members
 * (https://tools.ietf.org/html/rfc7946#section-6.1)
 * Developers should use "&" type in TypeScript or extend the interface
 * to add these foreign members.
 */
external interface GeoJsonObject {
// Don't include foreign members directly into this type def.
// in order to preserve type safety.
// [key: string]: any;
  /**
   * Specifies the type of GeoJSON object.
   */
  var type: GeoJsonTypes
  /**
   * Bounding box of the coordinate range of the object's Geometries, Features, or Feature Collections.
   * The value of the bbox member is an array of length 2*n where n is the number of dimensions
   * represented in the contained geometries, with all axes of the most southwesterly point
   * followed by all axes of the more northeasterly point.
   * The axes order of a bbox follows the axes order of geometries.
   * https://tools.ietf.org/html/rfc7946#section-5
   */
  var bbox: BBox?
}

/**
 * Union of GeoJSON objects.
 */
typealias GeoJSON = Any /* Geometry | Feature | FeatureCollection */

/**
 * Geometry object.
 * https://tools.ietf.org/html/rfc7946#section-3
 */
typealias Geometry = GeoJsonObject /* Point | MultiPoint | LineString | MultiLineString | Polygon | MultiPolygon | GeometryCollection */

/**
 * Point geometry object.
 * https://tools.ietf.org/html/rfc7946#section-3.1.2
 */
external interface Point : GeoJsonObject {
  override var type: GeoJsonTypes // Point
  var coordinates: Position
}

/**
 * MultiPoint geometry object.
 *  https://tools.ietf.org/html/rfc7946#section-3.1.3
 */
external interface MultiPoint : GeoJsonObject {
  override var type: GeoJsonTypes /* "MultiPoint" */
  var coordinates: Array<Position>
}

/**
 * LineString geometry object.
 * https://tools.ietf.org/html/rfc7946#section-3.1.4
 */
external interface LineString : GeoJsonObject {
  override var type: GeoJsonTypes /* "LineString" */
  var coordinates: Array<Position>
}

/**
 * MultiLineString geometry object.
 * https://tools.ietf.org/html/rfc7946#section-3.1.5
 */
external interface MultiLineString : GeoJsonObject {
  override var type: GeoJsonTypes /* "MultiLineString" */
  var coordinates: Array<Array<Position>>
}

/**
 * Polygon geometry object.
 * https://tools.ietf.org/html/rfc7946#section-3.1.6
 */
external interface Polygon : GeoJsonObject {
  override var type: GeoJsonTypes /* "Polygon" */
  var coordinates: Array<Array<Position>>
}

/**
 * MultiPolygon geometry object.
 * https://tools.ietf.org/html/rfc7946#section-3.1.7
 */
external interface MultiPolygon : GeoJsonObject {
  override var type: GeoJsonTypes /* "MultiPolygon" */
  var coordinates: Array<Array<Array<Position>>>
}

/**
 * Geometry Collection
 * https://tools.ietf.org/html/rfc7946#section-3.1.8
 */
external interface GeometryCollection<G : Geometry /* default is Geometry */> : GeoJsonObject {
  override var type: GeoJsonTypes /* "GeometryCollection" */
  var geometries: Array<G>
}

/**
 * A feature object which contains a geometry and associated properties.
 * https://tools.ietf.org/html/rfc7946#section-3.2
 */
external interface Feature<G : Geometry? /* default is Geometry */, P /* default is GeoJsonProperties */> : GeoJsonObject {
  override var type: GeoJsonTypes /* "Feature" */
  /**
   * The feature's geometry
   */
  var geometry: G
  /**
   * A value that uniquely identifies this feature in a
   * https://tools.ietf.org/html/rfc7946#section-3.2.
   */
  var id: String /* string | number | undefined */
  /**
   * Properties associated with this feature.
   */
  var properties: P
}

/**
 * A collection of feature objects.
 *  https://tools.ietf.org/html/rfc7946#section-3.3
 */
external interface FeatureCollection<G : Geometry /* default is Geometry */, P /* default is GeoJsonProperties */> : GeoJsonObject {
  override var type: GeoJsonTypes /* "FeatureCollection" */
  var features: Array<Feature<G, P>>
}
external interface GeoJsonProperties {
  @seskar.js.JsNative
  operator fun get(key: String): Any?

  @seskar.js.JsNative
  operator fun set(key: String, value: Any?)
}