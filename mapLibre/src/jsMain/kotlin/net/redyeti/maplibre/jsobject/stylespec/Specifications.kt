package net.redyeti.maplibre.jsobject.stylespec

import kotlinx.js.JsPlainObject
import seskar.js.JsValue

@JsPlainObject
external interface StyleSpecification {
  val version: Int
  val name: String
  val metadata: Any
  val center: Array<Double>
  val zoom: Double
  val bearing: Double
  val pitch: Double
  val light: LightSpecification?
  val sky: SkySpecification?

  //val projection: ProjectionSpecification?
  //val terrain: TerrainSpecification?
  val sources: Map<String, SourceSpecification>

  //val sprite: SpriteSpecification?
  val glyphs: String?
  //val transition: TransitionSpecification?
  //val layers: Array<LayerSpecification>
}

@JsPlainObject
external interface LightSpecification {
  val anchor: PropertyValueSpecification<ViewType>
  //position: PropertyValueSpecification<[ Double, Double, Double ]>
  //color: PropertyValueSpecification<ColorSpecification>
  //intensity: PropertyValueSpecification<Double>
}

@JsPlainObject
external interface SkySpecification {
//  sky-color: PropertyValueSpecification<ColorSpecification>
//  horizon-color: PropertyValueSpecification<ColorSpecification>
//  fog-color: PropertyValueSpecification<ColorSpecification>
//  fog-ground-blend: PropertyValueSpecification<Double>
//  horizon-fog-blend: PropertyValueSpecification<Double>
//  sky-horizon-blend: PropertyValueSpecification<Double>
//  atmosphere-blend: PropertyValueSpecification<Double>
}

@JsPlainObject
external interface TerrainSpecification {
  val source: String
  val exaggeration: Double
}

@JsPlainObject
external interface ProjectionSpecification {
  //type: mercator | globe
}

sealed external interface SourceType {
  companion object {
    @JsValue("vector")
    val Vector: SourceType
    @JsValue("raster")
    val Raster: SourceType
    @JsValue("raster-dem")
    val RasterDEM: SourceType
    @JsValue("geojson")
    val GeoJSON: SourceType
    @JsValue("video")
    val Video: SourceType
    @JsValue("image")
    val Image: SourceType
    @JsValue("canvas")
    val Canvas: SourceType
  }
}

@JsPlainObject
external interface SourceSpecification {
  val type: SourceType?
}

@JsPlainObject
external interface VectorSourceSpecification : SourceSpecification {
  val url: String?
  val tiles: Array<String>?

  val bounds: Array<Double> // [ Double, Double, Double, Double]
  //scheme: xyz | tms
  val minzoom: Double?
  val maxzoom: Double?
  val attribution: String?

  //val promoteId: PromoteIdSpecification
  val volatile: Boolean?
}

@JsPlainObject
external interface RasterSourceSpecification : SourceSpecification {
  val url: String?
  val tiles: Array<String>?

  val bounds: Array<Double> // [ Double, Double, Double, Double ]
  val minzoom: Double?
  val maxzoom: Double?
  val tileSize: Double?

  //scheme: xyz | tms
  val attribution: String?
  val volatile: Boolean?
}

@JsPlainObject
external interface RasterDEMSourceSpecification : SourceSpecification {
  val url: String?
  val tiles: Array<String>?

  //val bounds: [ Double, Double, Double, Double ]
  val minzoom: Double?
  val maxzoom: Double?
  val tileSize: Double?
  val attribution: String?

  //val encoding: terrarium | mapbox | custom
  val redFactor: Double?
  val blueFactor: Double?
  val greenFactor: Double?
  val baseShift: Double?
  val volatile: Boolean?
}

@JsPlainObject
external interface GeoJSONSourceSpecification : SourceSpecification {
  val data: Any // GeoJSON.GeoJSON | String
  val maxzoom: Double?
  val attribution: String?
  val buffer: Double?
  val filter: Any?
  val tolerance: Double?
  val cluster: Boolean?
  val clusterRadius: Double?
  val clusterMaxZoom: Double?
  val clusterMinPoints: Double?
  val clusterProperties: Any?
  val lineMetrics: Boolean?
  val generateId: Boolean?
  //val promoteId: PromoteIdSpecification?
}

@JsPlainObject
external interface VideoSourceSpecification : SourceSpecification {
  val urls: Array<String>
  //var coordinates: [ [ Double, Double ], [ Double, Double ], [ Double, Double ], [ Double, Double ] ]
}

@JsPlainObject
external interface ImageSourceSpecification : SourceSpecification {
  val url: String
  //var coordinates: [ [ Double, Double ], [ Double, Double ], [ Double, Double ], [ Double, Double ] ]
}

@JsPlainObject
external interface CanvasSourceSpecification : SourceSpecification {
  /**
   * Source type. Must be `"canvas"`.
   */
  //var type: canvas
  /**
   * Four geographical coordinates denoting where to place the corners of the canvas, specified in `[longitude, latitude]` pairs.
   */
  //var coordinates: [[Double, Double], [Double, Double], [Double, Double], [Double, Double]]
  /**
   * Whether the canvas source is animated. If the canvas is static (i.e. pixels do not need to be re-read on every frame), `animate` should be set to `false` to improve performance.
   * @defaultValue true
   */
  val animate: Boolean
  /**
   * Canvas source from which to read pixels. Can be a string representing the ID of the canvas element, or the `HTMLCanvasElement` itself.
   */
  //var canvas: String | HTMLCanvasElement
}

sealed external interface LayerType {
  companion object {
    @JsValue("fill")
    val Fill: LayerType
    @JsValue("line")
    val Line: LayerType
    @JsValue("symbol")
    val Symbol: LayerType
    @JsValue("circle")
    val Circle: LayerType
    @JsValue("heatmap")
    val HeatMap: LayerType
    @JsValue("fill-extrusion")
    val FillExtrusion: LayerType
    @JsValue("raster")
    val Raster: LayerType
    @JsValue("hillshade")
    val HillShade: LayerType
    @JsValue("background")
    val Background: LayerType
  }
}

typealias FilterSpecification = Any

@JsPlainObject
external interface LayerSpecification {
  var id: String
  var type: LayerType
  var metadata: Any?
  var minzoom: Double?
  var maxzoom: Double?
}

@JsPlainObject
external interface SourceLayerSpecification: LayerSpecification {
  var source: String
  @JsName("source-layer")
  val sourceLayer: String?
}

sealed external interface ViewType {
  companion object {
    val map: ViewType
    val viewport: ViewType
  }
}

sealed external interface ViewTypeAuto {
  companion object {
    val map: ViewType
    val viewport: ViewType
    val auto: ViewType
  }
}

sealed external interface ResampleType {
  companion object {
    val linear: ResampleType
    val nearest: ResampleType
  }
}

sealed external interface Visibility {
  companion object {
    val visible: Visibility
    val none: Visibility
  }
}

typealias FormattedSpecification = String
typealias ResolvedImageSpecification = String
typealias PaddingSpecification = Double // Double | Double[]
typealias VariableAnchorOffsetCollectionSpecification = Array<String> // Array<String | [ Double, Double ]>;

@JsPlainObject
external interface LayoutConfig {
  val visibility: Visibility?
}

@JsPlainObject
external interface FillLayoutConfig: LayoutConfig {
  @JsName("fill-sort-key")
  val fillSortKey: DataDrivenPropertyValueSpecification<Double>
}

sealed external interface LineCapType {
  companion object {
    @JsValue("butt")
    val Butt: LineCapType
    @JsValue("round")
    val Round: LineCapType
    @JsValue("square")
    val Square: LineCapType
  }
}

sealed external interface LineJoinType {
  companion object {
    @JsValue("bevel")
    val Bevel: LineJoinType
    @JsValue("round")
    val Round: LineJoinType
    @JsValue("miter")
    val Miter: LineJoinType
  }
}

@JsPlainObject
external interface LineLayoutConfig: LayoutConfig {
  @JsName("line-cap")
  var lineCap: PropertyValueSpecification<LineCapType>?
  @JsName("line-join")
  var lineJoin: DataDrivenPropertyValueSpecification<LineJoinType>?
  @JsName("line-miter-limit")
  var lineMiterLimit: PropertyValueSpecification<Double>?
  @JsName("line-round-limit")
  var lineRoundLimit: PropertyValueSpecification<Double>?
  @JsName("line-sort-key")
  var lineSortKey: DataDrivenPropertyValueSpecification<Double>?
}

@JsPlainObject
external interface CircleLayoutConfig: LayoutConfig {
  @JsName("circle-sort-key")
  var circleSortKey: DataDrivenPropertyValueSpecification<Double>?
}

@JsPlainObject
external interface SymbolLayoutConfig: LayoutConfig {
  @JsName("symbol-placement")
  val symbolPlacement: PropertyValueSpecification<String>? // "point" | "line" | "line-center"
  @JsName("symbol-spacing")
  val symbolSpacing: PropertyValueSpecification<Double>?
  @JsName("symbol-avoid-edges")
  val symbolAvoidEdges: PropertyValueSpecification<Boolean>?
  @JsName("symbol-sort-key")
  val symbolSortKey: DataDrivenPropertyValueSpecification<Double>?
  @JsName("symbol-z-order")
  val symbolZOrder: PropertyValueSpecification<String>? // "auto" | "viewport-y" | "source"
  @JsName("icon-allow-overlap")
  val iconAllowOverlap: PropertyValueSpecification<Boolean>?
  @JsName("icon-overlap")
  val iconOverlap: PropertyValueSpecification<String>? // "never" | "always" | "cooperative"
  @JsName("icon-ignore-placement")
  val iconIgnorePlacement: PropertyValueSpecification<Boolean>?
  @JsName("icon-optional")
  val iconOptional: PropertyValueSpecification<Boolean>?
  @JsName("icon-rotation-alignment")
  val iconRotationAlignment: PropertyValueSpecification<ViewTypeAuto>?
  @JsName("icon-size")
  val iconSize: DataDrivenPropertyValueSpecification<Double>?
  @JsName("icon-text-fit")
  val iconTextFit: PropertyValueSpecification<String>? // "none" | "width" | "height" | "both"
  @JsName("icon-text-fit-padding")
  val iconTextFitPadding: PropertyValueSpecification<Array<Double>>? // [Double, Double, Double, Double]
  @JsName("icon-image")
  var iconImage: DataDrivenPropertyValueSpecification<ResolvedImageSpecification>?
  @JsName("icon-rotate")
  val iconRotate: DataDrivenPropertyValueSpecification<Double>?
  @JsName("icon-padding")
  val iconPadding: DataDrivenPropertyValueSpecification<PaddingSpecification>?
  @JsName("icon-keep-upright")
  val iconKeepUpright: PropertyValueSpecification<Boolean>?
  @JsName("icon-offset")
  val iconOffset: DataDrivenPropertyValueSpecification<Array<Double>>? // [Double, Double]
  @JsName("icon-anchor")
  val iconAnchor: DataDrivenPropertyValueSpecification<String>? // "center" | "left" | "right" | "top" | "bottom" | "top-left" | "top-right" | "bottom-left" | "bottom-right"
  @JsName("icon-pitch-alignment")
  val iconPitchAlignment: PropertyValueSpecification<ViewTypeAuto>?
  @JsName("text-pitch-alignment")
  val textPitchAlignment: PropertyValueSpecification<ViewTypeAuto>?
  @JsName("text-rotation-alignment")
  val textRotationAlignment: PropertyValueSpecification<String>? // "map" | "viewport" | "viewport-glyph" | "auto"
  @JsName("text-field")
  var textField: String? // DataDrivenPropertyValueSpecification<FormattedSpecification>?
  @JsName("text-font")
  var textFont: Array<String>? // DataDrivenPropertyValueSpecification<Array<String>>?
  @JsName("text-size")
  var textSize: Double? // DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-max-width")
  val textMaxWidth: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-line-height")
  val textLineHeight: PropertyValueSpecification<Double>?
  @JsName("text-letter-spacing")
  val textLetterSpacing: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-justify")
  val textJustify: DataDrivenPropertyValueSpecification<String>? // "auto" | "left" | "center" | "right"
  @JsName("text-radial-offset")
  val textRadialOffset: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-variable-anchor")
  val textVariableAanchor: PropertyValueSpecification<Array<String>>? // "center" | "left" | "right" | "top" | "bottom" | "top-left" | "top-right" | "bottom-left" | "bottom-right"
  @JsName("text-variable-anchor-offset")
  val textVariableAnchorOffset: DataDrivenPropertyValueSpecification<VariableAnchorOffsetCollectionSpecification>?
  @JsName("text-anchor")
  val textAnchor: DataDrivenPropertyValueSpecification<String>? // "center" | "left" | "right" | "top" | "bottom" | "top-left" | "top-right" | "bottom-left" | "bottom-right"
  @JsName("text-max-angle")
  val textMaxAngle: PropertyValueSpecification<Double>?
  @JsName("text-writing-mode")
  val textWritingMode: PropertyValueSpecification<Array<String>>? // "horizontal" | "vertical"
  @JsName("text-rotate")
  val textRotate: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-padding")
  val textPadding: PropertyValueSpecification<Double>?
  @JsName("text-keep-upright")
  val textKeepUpright: PropertyValueSpecification<Boolean>?
  @JsName("text-transform")
  val textTransform: DataDrivenPropertyValueSpecification<String>? // "none" | "uppercase" | "lowercase"
  @JsName("text-offset")
  val textOffset: DataDrivenPropertyValueSpecification<Array<Double>>? // [Double, Double]
  @JsName("text-allow-overlap")
  val textAllowOverlap: PropertyValueSpecification<Boolean>?
  @JsName("text-overlap")
  val textOverlap: PropertyValueSpecification<String>? // "never" | "always" | "cooperative"
  @JsName("text-ignore-placement")
  val textIgnorePlacement: PropertyValueSpecification<Boolean>?
  @JsName("text-optional")
  val textOptional: PropertyValueSpecification<Boolean>?
}

@JsPlainObject
external interface FillPaintConfig {
  @JsName("fill-antialias")
  val fillAntialias: PropertyValueSpecification<Boolean>?
  @JsName("fill-opacity")
  val fillOpacity: DataDrivenPropertyValueSpecification<Double>?
  @JsName("fill-color")
  val fillColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("fill-outline-color")
  val fillOutlineColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("fill-translate")
  val fillTranslate: PropertyValueSpecification<Array<Double>>?  // [Double, Double]
  @JsName("fill-translate-anchor")
  val fillTranslateAnchor: PropertyValueSpecification<ViewType>?
  @JsName("fill-pattern")
  val fillPattern: DataDrivenPropertyValueSpecification<ResolvedImageSpecification>?
}

@JsPlainObject
external interface SymbolPaintConfig {
  @JsName("icon-opacity")
  val iconOpacity: DataDrivenPropertyValueSpecification<Double>?
  @JsName("icon-color")
  val iconColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("icon-halo-color")
  val iconHaloColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("icon-halo-width")
  val iconHaloWidth: DataDrivenPropertyValueSpecification<Double>?
  @JsName("icon-halo-blur")
  val iconHaloBlur: DataDrivenPropertyValueSpecification<Double>?
  @JsName("icon-translate")
  val iconTranslate: PropertyValueSpecification<Array<Double>>? // [Double, Double]
  @JsName("icon-translate-anchor")
  val iconTranslateAnchor: PropertyValueSpecification<ViewType>?
  @JsName("text-opacity")
  val textOpacity: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-color")
  var textColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("text-halo-color")
  val textHaloColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("text-halo-width")
  val textHaloWidth: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-halo-blur")
  val textHaloBlur: DataDrivenPropertyValueSpecification<Double>?
  @JsName("text-translate")
  val textTranslate: PropertyValueSpecification<Array<Double>>? // [Double, Double]
  @JsName("text-translate-anchor")
  val textTranslateAnchor: PropertyValueSpecification<ViewType>?
}

typealias ColorSpecification = String

@JsPlainObject
external interface CirclePaintConfig {
  @JsName("circle-radius")
  var circleRadius: DataDrivenPropertyValueSpecification<Double>?
  @JsName("circle-color")
  var circleColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("circle-blur")
  var circleBlur: DataDrivenPropertyValueSpecification<Double>?
  @JsName("circle-opacity")
  var circleOpacity: DataDrivenPropertyValueSpecification<Double>?
  @JsName("circle-translate")
  var circleTranslate: PropertyValueSpecification<Array<Double>>?
  @JsName("circle-translate-anchor")
  var circleTranslateAnchor: PropertyValueSpecification<ViewType>?
  @JsName("circle-pitch-scale")
  var circlePitchScale: PropertyValueSpecification<ViewType>?
  @JsName("circle-pitch-alignment")
  var circlePitchAlignment: PropertyValueSpecification<ViewType>?
  @JsName("circle-stroke-width")
  var circleStrokeWidth: DataDrivenPropertyValueSpecification<Double>?
  @JsName("circle-stroke-color")
  var circleStrokeColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("circle-stroke-opacity")
  var circleStrokeOpacity: DataDrivenPropertyValueSpecification<Double>?
}

@JsPlainObject
external interface HeatmapPaintConfig {
  @JsName("heatmap-radius")
  var heatmapRadius: DataDrivenPropertyValueSpecification<Double>?
  @JsName("heatmap-weight")
  var heatmapWeight: DataDrivenPropertyValueSpecification<Double>?
  @JsName("heatmap-intensity")
  var heatmapIntensity: PropertyValueSpecification<Double>?
  @JsName("heatmap-color")
  var heatmapColor: ExpressionSpecification?
  @JsName("heatmap-opacity")
  var heatmapOpacity: PropertyValueSpecification<Double>?
}

@JsPlainObject
external interface FillExtrusionPaintConfig {
  @JsName("fill-extrusion-opacity")
  val fillExtrusionOpacity: PropertyValueSpecification<Double>?
  @JsName("fill-extrusion-color")
  val fillExtrusionColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("fill-extrusion-translate")
  val fillExtrusionTranslate: PropertyValueSpecification<Array<Double>>?
  @JsName("fill-extrusion-translate-anchor")
  val fillExtrusionTranslateAnchor: PropertyValueSpecification<ViewType>?
  @JsName("fill-extrusion-pattern")
  val fillExtrusionPattern: DataDrivenPropertyValueSpecification<ResolvedImageSpecification>?
  @JsName("fill-extrusion-height")
  val fillExtrusionHeight: DataDrivenPropertyValueSpecification<Double>?
  @JsName("fill-extrusion-base")
  val fillExtrusionBase: DataDrivenPropertyValueSpecification<Double>?
  @JsName("fill-extrusion-vertical-gradient")
  val fillExtrusionVerticalGradient: PropertyValueSpecification<Boolean>?
}

@JsPlainObject
external interface LinePaintConfig {
  @JsName("line-opacity")
  var lineOpacity: PropertyValueSpecification<Double>?
  @JsName("line-color")
  var lineColor: DataDrivenPropertyValueSpecification<ColorSpecification>?
  @JsName("line-translate")
  var lineTranslate: PropertyValueSpecification<Array<Double>>?
  @JsName("line-translate-anchor")
  var lineTranslateAnchor: PropertyValueSpecification<ViewType>?
  @JsName("line-width")
  var lineWidth: DataDrivenPropertyValueSpecification<Double>?
  @JsName("line-gap-width")
  var lineGapWidth: DataDrivenPropertyValueSpecification<Double>?
  @JsName("line-offset")
  var lineOffset: DataDrivenPropertyValueSpecification<Double>?
  @JsName("line-blur")
  var lineBlur: PropertyValueSpecification<Double>?
  @JsName("line-dasharray")
  var lineDashArray: PropertyValueSpecification<Double>?
  @JsName("line-pattern")
  var linePattern: DataDrivenPropertyValueSpecification<ResolvedImageSpecification>?
  @JsName("line-gradient")
  var lineGradient: DataDrivenPropertyValueSpecification<ColorSpecification>?
}

@JsPlainObject
external interface RasterPaintConfig {
  @JsName("raster-opacity")
  val rasterOpacity: PropertyValueSpecification<Double>?
  @JsName("raster-hue-rotate")
  val rasterHueRotate: PropertyValueSpecification<Double>?
  @JsName("raster-brightness-min")
  val rasterBrightnessMin: PropertyValueSpecification<Double>?
  @JsName("raster-brightness-max")
  val rasterBrightnessMax: PropertyValueSpecification<Double>?
  @JsName("raster-saturation")
  val rasterSaturation: PropertyValueSpecification<Double>?
  @JsName("raster-contrast")
  val rasterContrast: PropertyValueSpecification<Double>?
  @JsName("raster-resampling")
  val rasterResampling: PropertyValueSpecification<ResampleType>?
  @JsName("raster-fade-duration")
  val rasterFadeDuration: PropertyValueSpecification<Double>?
}

@JsPlainObject
external interface HillshadePaintConfig {
  @JsName("hillshade-illumination-direction")
  val hillshadeIlluminationDirection: PropertyValueSpecification<Double>?
  @JsName("hillshade-illumination-anchor")
  val hillshadeIlluminationAnchor: PropertyValueSpecification<ViewType>?
  @JsName("hillshade-exaggeration")
  val hillshadeExaggeration: PropertyValueSpecification<Double>?
  @JsName("hillshade-shadow-color")
  val hillshadeShadowColor: PropertyValueSpecification<ColorSpecification>?
  @JsName("hillshade-highlight-color")
  val hillshadeHighlightColor: PropertyValueSpecification<ColorSpecification>?
  @JsName("hillshade-accent-color")
  val hillshadeAccentColor: PropertyValueSpecification<ColorSpecification>?
}

@JsPlainObject
external interface BackgroundPaintConfig {
  @JsName("background-color")
  val backgroundColor: PropertyValueSpecification<ColorSpecification>?
  @JsName("background-pattern")
  val backgroundPattern: PropertyValueSpecification<ResolvedImageSpecification>?
  @JsName("background-opacity")
  val backgroundOpacity: PropertyValueSpecification<Double>?
}

@JsPlainObject
external interface FillLayerSpecification : SourceLayerSpecification {
  val filter: FilterSpecification?
  val layout: FillLayoutConfig?
  val paint: FillPaintConfig?
}

@JsPlainObject
external interface LineLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: LineLayoutConfig?
  var paint: LinePaintConfig?
}

@JsPlainObject
external interface SymbolLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: SymbolLayoutConfig?
  var paint: SymbolPaintConfig?
}

@JsPlainObject
external interface CircleLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: CircleLayoutConfig?
  var paint: CirclePaintConfig?
}

@JsPlainObject
external interface HeatmapLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: LayoutConfig?
  var paint: HeatmapPaintConfig?
}

@JsPlainObject
external interface FillExtrusionLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: LayoutConfig?
  var paint: FillExtrusionPaintConfig?
}

@JsPlainObject
external interface RasterLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: LayoutConfig?
  var paint: RasterPaintConfig?
}


@JsPlainObject
external interface HillshadeLayerSpecification : SourceLayerSpecification {
  var filter: FilterSpecification?
  var layout: LayoutConfig?
  var paint: HillshadePaintConfig?
}

@JsPlainObject
external interface BackgroundLayerSpecification : LayerSpecification {
  val layout: LayoutConfig?
  val paint: BackgroundPaintConfig?
}

typealias PropertyValueSpecification<T> = Any
typealias DataDrivenPropertyValueSpecification<T> = Any
typealias ExpressionSpecification = Any

//external interface CameraFunctionSpecification<T>: DataDrivenPropertyValueSpecification<T>
//{ type: 'exponential', stops: Array<[Double, T]> }
//| { type: 'interval',    stops: Array<[Double, T]> };
//
//export type SourceFunctionSpecification<T> =
//{ type: 'exponential', stops: Array<[Double, T]>, property: String, default?: T }
//| { type: 'interval',    stops: Array<[Double, T]>, property: String, default?: T }
//| { type: 'categorical', stops: Array<[String | Double | Boolean, T]>, property: String, default?: T }
//| { type: 'identity', property: string, default?: T };
//
//export type CompositeFunctionSpecification<T> =
//{ type: 'exponential', stops: Array<[{zoom: Double, value: Double}, T]>, property: String, default?: T }
//| { type: 'interval',    stops: Array<[{zoom: Double, value: Double}, T]>, property: String, default?: T }
//| { type: 'categorical', stops: Array<[{zoom: Double, value: String | Double | boolean}, T]>, property: String, default?: T };

