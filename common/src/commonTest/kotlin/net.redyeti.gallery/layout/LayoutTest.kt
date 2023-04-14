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
    with(AlbumLayout.compute(config, data, items)) {
      assertEquals(110, containerHeight)
      assertEquals(0, widowCount)
      assertEquals(1, boxes.size)
      with(boxes.first()) {
        assertEquals(10, top)
        assertEquals(10, left)
        assertEquals(180, width)
        assertEquals(90, height)
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
    with(AlbumLayout.compute(config, data, items)) {
      assertEquals(items.size, boxes.size)
      assertEquals(194, boxes[0].height)
      assertEquals(194, boxes[1].height)
      assertEquals(194, boxes[2].height)
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
