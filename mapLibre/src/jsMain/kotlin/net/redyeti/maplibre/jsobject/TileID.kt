package net.redyeti.maplibre.jsobject

import net.redyeti.maplibre.jsobject.geo.MercatorCoordinate
import net.redyeti.maplibre.jsobject.stylespec.ICanonicalTileID
import net.redyeti.maplibre.jsobject.stylespec.IMercatorCoordinate

/**
 * A canonical way to define a tile ID
 */
external class CanonicalTileID {
  var z: Double
  var x: Double
  var y: Double
  var key: String

  constructor(z: Double, x: Double, y: Double)

  fun equals(id: CanonicalTileID): Boolean

  // given a list of urls, choose a url template and return a tile URL
  fun url(urls: Array<String>, pixelRatio: Double, scheme: String?)

  fun isChildOf(parent: CanonicalTileID): Boolean

  fun getTilePoint(coord: IMercatorCoordinate): Point

  override fun toString(): String
}

/**
 * @internal
 * An unwrapped tile identifier
 */
external class UnwrappedTileID {
  var wrap: Double
  var canonical: CanonicalTileID
  var key: String

  constructor(wrap: Double, canonical: CanonicalTileID)
}

/**
 * An overscaled tile identifier
 */
external class OverscaledTileID(overscaledZ: Double, wrap: Double, z: Double, x: Double, y: Double) {
  var overscaledZ: Double
  var wrap: Double
  var canonical: CanonicalTileID
  var key: String
  /**
   * This matrix is used during terrain's render-to-texture stage only.
   * If the render-to-texture stage is active, this matrix will be present
   * and should be used, otherwise this matrix will be null.
   */
  // var posMatrix: mat4
  fun clone(): OverscaledTileID
  fun equals(id: OverscaledTileID): Boolean
  fun scaledTo(targetZ: Double): OverscaledTileID
  /*
   * calculateScaledKey is an optimization:
   * when withWrap == true, implements the same as this.scaledTo(z).key,
   * when withWrap == false, implements the same as this.scaledTo(z).wrapped().key.
   */
  fun calculateScaledKey(targetZ: Double, withWrap: Boolean): String
  fun isChildOf(parent: OverscaledTileID): Boolean
  fun children(sourceMaxZoom: Double): Array<OverscaledTileID>
  fun isLessThan(rhs: OverscaledTileID): Boolean
  fun wrapped(): OverscaledTileID
  fun unwrapTo(wrap: Double): OverscaledTileID
  fun overscaleFactor(): Double

  fun toUnwrapped(): UnwrappedTileID
  override fun toString(): String
  fun getTilePoint(coord: MercatorCoordinate): Point
}

external fun calculateTileKey(wrap: Double, overscaledZ: Double, z: Double, x: Double, y: Double): String

//fun getQuadkey(z, x, y) {
//  let quadkey = '', mask;
//  for (let i = z; i > 0; i--) {
//    mask = 1 << (i - 1);
//    quadkey += ((x & mask ? 1 : 0) + (y & mask ? 2 : 0));
//  }
//  return quadkey;
//}
