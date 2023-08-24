package net.redyeti.gallery.layout

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class ContainerPadding(val top: Int, val left: Int, val bottom: Int, val right: Int) {
  companion object {
    val None = ContainerPadding(0, 0, 0, 0)
  }
}

class BoxSpacing(val horizontal: Int, val vertical: Int)

class LayoutConfig(
  val width: Int = 1000,
  val padding: ContainerPadding = ContainerPadding.None,
  val boxSpacing: BoxSpacing = BoxSpacing(10, 10),
  val targetRowHeight: Int = 200,
  val tolerance: Double = 0.2
) {
  var widowCount = 0
}

data class Box(var top: Int, var left: Int, var width: Int, var height: Int)

class LayoutData(var containerHeight: Int = 10) {
  val layoutBoxes = mutableListOf<Box>()
  val rows = mutableListOf<Row>()
}

class LayoutResult(val containerHeight: Int, val widowCount: Int, val boxes: List<Box>)

object AlbumLayout {
  fun addRow(layoutConfig: LayoutConfig, layoutData: LayoutData, row: Row): List<Double> {
    with(layoutData) {
      rows += row
      layoutBoxes += row.boxes
      containerHeight += row.height + layoutConfig.boxSpacing.vertical
    }
    return row.itemAspectRatios
  }

  fun createRow(config: LayoutConfig, data: LayoutData): Row {
    return Row(
      data.containerHeight,
      config.padding.left,
      config.width - config.padding.left - config.padding.right,
      config.boxSpacing.horizontal,
      config.targetRowHeight,
      config.tolerance
    )
  }

  fun compute(config: LayoutConfig, data: LayoutData, aspectRatios: List<Double>): LayoutResult {
    val laidOutItems = mutableListOf<Double>()
    var currentRow: Row? = null
    aspectRatios.forEach { ratio ->
      var current = currentRow
      // Make a new row if one isn't already in progress
      if (current == null) {
        current = createRow(config, data)
      }

      // Attempt to add this item to the current row
      var itemWasAdded = current.addItem(ratio)

      if (current.isLayoutComplete()) {
        // The row is full, remember it and start a new row
        laidOutItems += addRow(config, data, current)
        current = createRow(config, data)

        if (!itemWasAdded) {
          // The item didn't fit in the previous row so add it to the new one
          current.addItem(ratio)
          if (current.isLayoutComplete()) {
            // If the rejected item fills the whole row, add it and start a new row
            laidOutItems += addRow(config, data, current)
            current = createRow(config, data)
          }
        }
      }
      currentRow = current
    }

    // Deal with any remaining items
    val current = currentRow
    if (current != null && current.itemAspectRatios.isNotEmpty()) {
      if (data.rows.isNotEmpty()) {
        current.completeLayout(data.rows.last().height, false)
      } else {
        current.completeLayout(current.targetHeight, false)
      }
      laidOutItems += addRow(config, data, current)
      config.widowCount = current.itemAspectRatios.size
    }

    data.containerHeight += config.padding.bottom - config.boxSpacing.vertical

    return LayoutResult(data.containerHeight, config.widowCount, data.layoutBoxes)
  }
}


class Row(
  val top: Int,
  val left: Int,
  val width: Int,
  val spacing: Int,
  val targetHeight: Int,
  val tolerance: Double,
  val minAllowedHeight: Int = targetHeight / 2,
  val maxAllowedHeight: Int = 2 * targetHeight
) {
  val minAspectRatio = width / targetHeight * (1 - tolerance)
  val maxAspectRatio = width / targetHeight * (1 + tolerance)
  var height = 0
  val itemAspectRatios = mutableListOf<Double>()
  val boxes = mutableListOf<Box>()

  fun addItem(aspectRatio: Double): Boolean {
    val widthWithoutSpacing = (width - itemAspectRatios.size * spacing).toDouble()
    val newAspectRatio = itemAspectRatios.sum() + aspectRatio

    if (newAspectRatio < minAspectRatio) {
      itemAspectRatios.add(aspectRatio)
      return true
    }
    if (newAspectRatio > maxAspectRatio) {
      if (itemAspectRatios.isEmpty()) {
        // A wide panorama, we have to accept this
        itemAspectRatios.add(aspectRatio)
        completeLayout((widthWithoutSpacing / newAspectRatio).roundToInt())
        return true
      }
      val targetAspectRatio = widthWithoutSpacing / targetHeight.toDouble()
      val prevWidthWithoutSpacing = width - (itemAspectRatios.size - 1) * spacing
      val prevAspectRatio = itemAspectRatios.sum()
      val prevTargetAspectRatio = prevWidthWithoutSpacing / targetHeight
      if (abs(newAspectRatio - targetAspectRatio) > abs(prevAspectRatio - prevTargetAspectRatio)) {
        completeLayout((prevWidthWithoutSpacing.toDouble() / prevAspectRatio).roundToInt())
        return false
      }
      itemAspectRatios.add(aspectRatio)
      completeLayout((widthWithoutSpacing / newAspectRatio).roundToInt())
      return true
    }
    itemAspectRatios.add(aspectRatio)
    completeLayout((widthWithoutSpacing / newAspectRatio).roundToInt())
    return true
  }

  fun isLayoutComplete() = height > 0

  fun completeLayout(newHeight: Int, justify: Boolean = true) {
    val clampedHeight = max(minAllowedHeight, min(newHeight, maxAllowedHeight))
    val clampedToNativeRatio: Double
    if (newHeight != clampedHeight) {
      height = clampedHeight
      clampedToNativeRatio = newHeight.toDouble() / clampedHeight.toDouble()
    } else {
      height = newHeight
      clampedToNativeRatio = 1.0
    }

    var widthSum = left
    itemAspectRatios.forEach { ratio ->
      val box = Box(top, widthSum, (ratio * height * clampedToNativeRatio).roundToInt(), height)
      widthSum += box.width + spacing
      boxes += box
    }

    if (justify) {
      // Apply justification
      widthSum -= spacing + left
      val errorWidthPerItem = (widthSum - width).toDouble() / itemAspectRatios.size
      val roundedCumulativeErrors =
        List(itemAspectRatios.size) { i -> ((i + 1).toDouble() * errorWidthPerItem).roundToInt() }
      if (boxes.size == 1) {
        boxes.first().width -= errorWidthPerItem.roundToInt()
      } else {
        boxes.forEachIndexed { i, box ->
          if (i > 0) {
            box.left -= roundedCumulativeErrors[i - 1]
            box.width -= roundedCumulativeErrors[i] - roundedCumulativeErrors[i - 1]
          } else {
            box.width -= roundedCumulativeErrors[i]
          }
        }
      }
    }
  }
}