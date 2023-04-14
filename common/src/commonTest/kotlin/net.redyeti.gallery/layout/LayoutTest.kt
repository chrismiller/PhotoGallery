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
  fun testSimpleLayout() {
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
}
