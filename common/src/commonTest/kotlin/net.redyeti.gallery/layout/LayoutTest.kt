package net.redyeti.gallery.layout

import org.koin.test.KoinTest
import kotlin.test.Test

class LayoutTest : KoinTest {
  @Test
  fun testSimpleLayout() {
    val config = LayoutConfig(
      width = 200,
      targetRowHeight = 100,
      tolerance = 0.25
    )
    val data = LayoutData(config.padding.top)
    val items = listOf(0.5, 1.2, 1.1, 1.0)
    with(AlbumLayout.compute(config, data, items)) {
      println("Height $containerHeight, widows $widowCount, boxes $boxes")
    }
  }
}
