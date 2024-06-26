package net.redyeti.gallery.layout

import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LayoutTest : KoinTest {
  @Test
  fun testSingle() {
    val config = LayoutConfig(
      width = 200,
      targetRowHeight = 100,
      tolerance = 0.25
    )
    val data = LayoutData(config.padding.top)
    val items = listOf(2.0)
    val layoutResult = AlbumLayout.compute(config, data, items)
    with(layoutResult) {
      assertEquals(100, containerHeight)
      assertEquals(0, widowCount)
      assertEquals(1, boxes.size)
      with(boxes.first()) {
        assertEquals(0, top)
        assertEquals(0, left)
        assertEquals(200, width)
        assertEquals(100, height)
      }
    }
  }

  @Test
  fun testWrap() {
    val config = LayoutConfig(
      width = 1000,
      targetRowHeight = 250,
      tolerance = 0.25
    )
    val data = LayoutData(config.padding.top)
    val items = listOf(1.0, 4.0, 1.1)
    val layout = AlbumLayout.compute(config, data, items)
    with(layout) {
      assertEquals(items.size, boxes.size)
      assertEquals(198, boxes[0].width)
      assertEquals(198, boxes[0].height)
      assertEquals(config.width, boxes[0].width + config.boxSpacing.horizontal + boxes[1].width)
      assertEquals(218, boxes[2].width)
      assertEquals(198, boxes[2].height)  // TODO: should this be closer to ~250?
    }
  }

  @Test
  fun testMore() {
    val config = LayoutConfig(
      width = 1000,
      targetRowHeight = 200,
      tolerance = 0.2
    )
    val data = LayoutData(config.padding.top)
    val items = listOf(1.5, 1.5, 1.5, 1.5, 1.5)
    val layout = AlbumLayout.compute(config, data, items)
    with(layout) {
      assertEquals(items.size, boxes.size)
      assertEquals(327, boxes[0].width)
      assertEquals(218, boxes[0].height)
      val firstRow = 3
      assertEquals(1000, boxes.subList(0, firstRow).sumOf { it.width } + (firstRow - 1) * config.boxSpacing.horizontal)
      assertEquals(boxes[0].height, boxes[1].height)
      assertEquals(boxes[0].height + config.boxSpacing.vertical, boxes[3].top)
      assertEquals(boxes[0].height, boxes[1].height)
    }
  }

  @Test
  fun testNoPadding() {
    val config = LayoutConfig(
      width = 1000,
      targetRowHeight = 250,
      tolerance = 0.25,
      padding = ContainerPadding.None
    )
    val data = LayoutData(config.padding.top)
    val items = listOf(1.0, 3.0, 2.0, 1.2, 4.0)
    with(AlbumLayout.compute(config, data, items)) {
      assertEquals(items.size, boxes.size)
      assertEquals(247, boxes[0].width)
      assertEquals(248, boxes[0].height)
      assertEquals(743, boxes[1].width)
      assertEquals(248, boxes[1].height)
      assertEquals(618, boxes[2].width)
      assertEquals(309, boxes[2].height)
      assertEquals(372, boxes[3].width)
      assertEquals(309, boxes[3].height)
      assertEquals(1000, boxes[4].width)
      assertEquals(250, boxes[4].height)
    }
  }
}
