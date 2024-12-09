package net.redyeti.maplibre.jsobject

import kotlinx.js.JsPlainObject
import net.redyeti.maplibre.jsobject.stylespec.StyleSpecification

/**
 * The options object related to the {@link Map}'s style related methods
 */
@JsPlainObject
external interface StyleOptions {
  /**
   * If false, style validation will be skipped. Useful in production environment.
   */
  val validate: Boolean?
  /**
   * Defines a CSS
   * font-family for locally overriding generation of Chinese, Japanese, and Korean characters.
   * For these characters, font settings from the map's style will be ignored, except for font-weight keywords (light/regular/medium/bold).
   * Set to `false`, to enable font settings from the map's style for these glyph ranges.
   * Forces a full update.
   */
  val localIdeographFontFamily: String?
};

/**
 * Supporting type to add validation to another style related type
 */
@JsPlainObject
external interface StyleSetterOptions {
  /**
   * Whether to check if the filter conforms to the MapLibre Style Specification. Disabling validation is a performance optimization that should only be used if you have previously validated the values you will be passing to this function.
   */
  val validate: Boolean?
}

/**
 * Part of {@link Map#setStyle} options, transformStyle is a convenience function that allows to modify a style after it is fetched but before it is committed to the map state.
 *
 * This function exposes previous and next styles, it can be commonly used to support a range of functionalities like:
 *
 * - when previous style carries certain 'state' that needs to be carried over to a new style gracefully;
 * - when a desired style is a certain combination of previous and incoming style;
 * - when an incoming style requires modification based on external state.
 *
 * @param previous - The current style.
 * @param next - The next style.
 * @returns resulting style that will to be applied to the map
 *
 * @example
 * ```ts
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
typealias TransformStyleFunction = (previous: StyleSpecification?, next: StyleSpecification) -> StyleSpecification

/**
 * The options object related to the {@link Map}'s style related methods
 */
@JsPlainObject
external interface StyleSwapOptions {
  /**
   * If false, force a 'full' update, removing the current style
   * and building the given one instead of attempting a diff-based update.
   */
  val diff: Boolean?
  /**
   * TransformStyleFunction is a convenience function
   * that allows to modify a style after it is fetched but before it is committed to the map state. Refer to {@link TransformStyleFunction}.
   */
  val transformStyle: TransformStyleFunction?
}
