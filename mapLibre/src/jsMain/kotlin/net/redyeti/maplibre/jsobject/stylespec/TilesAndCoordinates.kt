package net.redyeti.maplibre.jsobject.stylespec

external interface ICanonicalTileID {
  var z: Double
  var x: Double
  var y: Double
  var key: String
  fun equals(id: ICanonicalTileID): Boolean
  fun url(urls: Array<String>, pixelRatio: Double, scheme: String?): String
  fun isChildOf(parent: ICanonicalTileID): Boolean
  fun getTilePoint(coord: IMercatorCoordinate): Point2D
  override fun toString(): String
}

external interface IMercatorCoordinate {
  var x: Double
  var y: Double
  var z: Double

  fun toLngLat(): ILngLat
  fun toAltitude(): Double
  fun meterInMercatorCoordinateUnits(): Double
}

external interface ILngLat {
  var lng: Double
  var lat: Double

  fun wrap(): ILngLat;
  fun toArray(): Array<Double>
  fun distanceTo(lngLat: ILngLat): Double
  override fun toString(): String
}
