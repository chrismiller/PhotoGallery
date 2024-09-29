@file:JsModule("maplibre-gl")
@file:JsNonModule

package net.redyeti.maplibre.jsobject

external class Point(x: Double, y: Double) {
  var x: Double
  var y: Double
}

/**
 * A [MapLibre GL JS LngLat](https://maplibre.org/maplibre-gl-js/docs/API/classes/LngLat/) object
 *
 * A `LngLat` object represents a given longitude and latitude coordinate, measured in degrees.
 * These coordinates are based on the [WGS84 (EPSG:4326) standard](https://en.wikipedia.org/wiki/World_Geodetic_System#WGS84).
 *
 * MapLibre GL JS uses longitude, latitude coordinate order (as opposed to latitude, longitude) to match the
 * [GeoJSON specification](https://tools.ietf.org/html/rfc7946).
 *
 * @group Geography and Geometry
 *
 * @example
 * ```ts
 * let ll = new LngLat(-123.9749, 40.7736);
 * ll.lng; // = -123.9749
 * ```
 */
external class LngLat(lng: Double, lat: Double) {
  /**
   * Longitude, measured in degrees.
   */
  var lat: Double

  /**
   * Latitude, measured in degrees.
   */
  var lng: Double

  /**
   * Returns a new `LngLat` object whose longitude is wrapped to the range (-180, 180).
   *
   * @returns The wrapped `LngLat` object.
   * @example
   * ```ts
   * let ll = new LngLat(286.0251, 40.7736);
   * let wrapped = ll.wrap();
   * wrapped.lng; // = -73.9749
   * ```
   */
  fun wrap(): LngLat

  /**
   * Returns the coordinates represented as an array of two numbers.
   *
   * @returns The coordinates represented as an array of longitude and latitude.
   * @example
   * ```ts
   * let ll = new LngLat(-73.9749, 40.7736);
   * ll.toArray(); // = [-73.9749, 40.7736]
   * ```
   */
  fun toArray(): Array<Double>

  /**
   * Returns the coordinates represent as a string.
   *
   * @returns The coordinates represented as a string of the format `'LngLat(lng, lat)'`.
   * @example
   * ```ts
   * let ll = new LngLat(-73.9749, 40.7736);
   * ll.toString(); // = "LngLat(-73.9749, 40.7736)"
   * ```
   */
  override fun toString(): String

  /**
   * Returns the approximate distance between a pair of coordinates in meters
   * Uses the Haversine Formula (from R.W. Sinnott, "Virtues of the Haversine", Sky and Telescope, vol. 68, no. 2, 1984, p. 159)
   *
   * @param lngLat - coordinates to compute the distance to
   * @returns Distance in meters between the two coordinates.
   * @example
   * ```ts
   * let new_york = new LngLat(-74.0060, 40.7128);
   * let los_angeles = new LngLat(-118.2437, 34.0522);
   * new_york.distanceTo(los_angeles); // = 3935751.690893987, "true distance" using a non-spherical approximation is ~3966km
   * ```
   */
  fun distanceTo(lngLat: LngLat): Double

  companion object {
    /**
     * Converts an array of two numbers or an object with `lng` and `lat` or `lon` and `lat` properties
     * to a `LngLat` object.
     *
     * If a `LngLat` object is passed in, the function returns it unchanged.
     *
     * @param input - An array of two numbers or object to convert, or a `LngLat` object to return.
     * @returns A new `LngLat` object, if a conversion occurred, or the original `LngLat` object.
     * @example
     * ```ts
     * let arr = [-73.9749, 40.7736];
     * let ll = LngLat.convert(arr);
     * ll;   // = LngLat {lng: -73.9749, lat: 40.7736}
     * ```
     */
    fun convert(input: LngLat): LngLat
  }
}

/**
 * A [MapLibre GL JS LngLatBounds](https://maplibre.org/maplibre-gl-js/docs/API/classes/LngLatBounds/) object
 */
external class LngLatBounds {
  /**
   * @param sw - The southwest corner of the bounding box.
   * OR array of 4 numbers in the order of  west, south, east, north
   * OR array of 2 LngLat: [sw,ne]
   * @param ne - The northeast corner of the bounding box.
   * @example
   * ```ts
   * let sw = new LngLat(-73.9876, 40.7661);
   * let ne = new LngLat(-73.9397, 40.8002);
   * let llb = new LngLatBounds(sw, ne);
   * ```
   * OR
   * ```ts
   * let llb = new LngLatBounds([-73.9876, 40.7661, -73.9397, 40.8002]);
   * ```
   * OR
   * ```ts
   * let llb = new LngLatBounds([sw, ne]);
   * ```
   */
  constructor (sw: LngLat, ne: LngLat)

  var _ne: LngLat
  var _sw: LngLat

  /**
   * Set the northeast corner of the bounding box
   *
   * @param ne - a {@link LngLat} object describing the northeast corner of the bounding box.
   * @returns `this`
   */
  fun setNorthEast(ne: LngLat): LngLatBounds

  /**
   * Set the southwest corner of the bounding box
   *
   * @param sw - a {@link LngLat} object describing the southwest corner of the bounding box.
   * @returns `this`
   */
  fun setSouthWest(sw: LngLat): LngLatBounds

  /**
   * Extend the bounds to include a given LngLat or LngLatBounds.
   *
   * @param obj - object to extend to
   * @returns `this`
   */
  fun extend(obj: LngLat): LngLatBounds

  fun extend(obj: LngLatBounds): LngLatBounds

  /**
   * Returns the geographical coordinate equidistant from the bounding box's corners.
   *
   * @returns The bounding box's center.
   * @example
   * ```ts
   * let llb = new LngLatBounds([-73.9876, 40.7661], [-73.9397, 40.8002]);
   * llb.getCenter(); // = LngLat {lng: -73.96365, lat: 40.78315}
   * ```
   */
  fun getCenter(): LngLat

  /**
   * Returns the southwest corner of the bounding box.
   *
   * @returns The southwest corner of the bounding box.
   */
  fun getSouthWest(): LngLat

  /**
   * Returns the northeast corner of the bounding box.
   *
   * @returns The northeast corner of the bounding box.
   */
  fun getNorthEast(): LngLat

  /**
   * Returns the northwest corner of the bounding box.
   *
   * @returns The northwest corner of the bounding box.
   */
  fun getNorthWest(): LngLat

  /**
   * Returns the southeast corner of the bounding box.
   *
   * @returns The southeast corner of the bounding box.
   */
  fun getSouthEast(): LngLat

  /**
   * Returns the west edge of the bounding box.
   *
   * @returns The west edge of the bounding box.
   */
  fun getWest(): Double

  /**
   * Returns the south edge of the bounding box.
   *
   * @returns The south edge of the bounding box.
   */
  fun getSouth(): Double

  /**
   * Returns the east edge of the bounding box.
   *
   * @returns The east edge of the bounding box.
   */
  fun getEast(): Double

  /**
   * Returns the north edge of the bounding box.
   *
   * @returns The north edge of the bounding box.
   */
  fun getNorth(): Double

  /**
   * Returns the bounding box represented as an array.
   *
   * @returns The bounding box represented as an array, consisting of the
   * southwest and northeast coordinates of the bounding represented as arrays of numbers.
   * @example
   * ```ts
   * let llb = new LngLatBounds([-73.9876, 40.7661], [-73.9397, 40.8002]);
   * llb.toArray(); // = [[-73.9876, 40.7661], [-73.9397, 40.8002]]
   * ```
   */
  fun toArray(): Array<Double>

  /**
   * Return the bounding box represented as a string.
   *
   * @returns The bounding box represents as a string of the format
   * `'LngLatBounds(LngLat(lng, lat), LngLat(lng, lat))'`.
   * @example
   * ```ts
   * let llb = new LngLatBounds([-73.9876, 40.7661], [-73.9397, 40.8002]);
   * llb.toString(); // = "LngLatBounds(LngLat(-73.9876, 40.7661), LngLat(-73.9397, 40.8002))"
   * ```
   */
  override fun toString(): String

  /**
   * Check if the bounding box is an empty/`null`-type box.
   *
   * @returns True if bounds have been defined, otherwise false.
   */
  fun isEmpty(): Boolean

  /**
   * Check if the point is within the bounding box.
   *
   * @param lnglat - geographic point to check against.
   * @returns `true` if the point is within the bounding box.
   * @example
   * ```ts
   * let llb = new LngLatBounds(
   *   new LngLat(-73.9876, 40.7661),
   *   new LngLat(-73.9397, 40.8002)
   * );
   *
   * let ll = new LngLat(-73.9567, 40.7789);
   *
   * console.log(llb.contains(ll)); // = true
   * ```
   */
  fun contains(lnglat: LngLat): Boolean

  companion object {
    /**
     * Converts an array to a `LngLatBounds` object.
     *
     * If a `LngLatBounds` object is passed in, the function returns it unchanged.
     *
     * Internally, the function calls `LngLat#convert` to convert arrays to `LngLat` values.
     *
     * @param input - An array of two coordinates to convert, or a `LngLatBounds` object to return.
     * @returns A new `LngLatBounds` object, if a conversion occurred, or the original `LngLatBounds` object.
     * @example
     * ```ts
     * let arr = [[-73.9876, 40.7661], [-73.9397, 40.8002]];
     * let llb = LngLatBounds.convert(arr); // = LngLatBounds {_sw: LngLat {lng: -73.9876, lat: 40.7661}, _ne: LngLat {lng: -73.9397, lat: 40.8002}}
     * ```
     */
    fun convert(input: LngLatBounds?): LngLatBounds

    /**
     * Returns a `LngLatBounds` from the coordinates extended by a given `radius`. The returned `LngLatBounds` completely contains the `radius`.
     *
     * @param center - center coordinates of the new bounds.
     * @param radius - Distance in meters from the coordinates to extend the bounds.
     * @returns A new `LngLatBounds` object representing the coordinates extended by the `radius`.
     * @example
     * ```ts
     * let center = new LngLat(-73.9749, 40.7736);
     * LngLatBounds.fromLngLat(100).toArray(); // = [[-73.97501862141328, 40.77351016847229], [-73.97478137858673, 40.77368983152771]]
     * ```
     */
    fun fromLngLat(center: LngLat, radius: Double = definedExternally): LngLatBounds
  }
}