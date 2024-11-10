package net.redyeti.maplibre.jsobject.geojson

/**
 * An extended geojson feature used by the events to return data to the listener
 */
external class MapGeoJSONFeature: GeoJSONFeature {
  var layer: Any // DistributiveOmit<LayerSpecification, 'source'> & {source: string};
  var source: String
  var sourceLayer: String
  var state: Map<String, Any>  // { [key: string]: any };
}

/**
 * A geojson feature
 */
open external class GeoJSONFeature(z: Double, x: Double, y: Double, id: Any /* number | string | undefined */) {
  val type: String
  val id: Any
  val geometry: Geometry
  val properties: Map<String, Any> // { [name: string]: any };
  // _vectorTileFeature: VectorTileFeature

  fun toJSON()
}