package net.redyeti.maplibre

import androidx.compose.runtime.AbstractApplier

internal interface MapNode {
  fun onAttached() {}
  fun onRemoved() {}
  fun onCleared() {}
}

private object MapNodeRoot : MapNode

internal class MapApplier: AbstractApplier<MapNode>(MapNodeRoot) {

  private val decorations = mutableListOf<MapNode>()

  override fun insertBottomUp(index: Int, instance: MapNode) {
    decorations.add(index, instance)
    instance.onAttached()
  }

  override fun insertTopDown(index: Int, instance: MapNode) { }

  override fun remove(index: Int, count: Int) {
    repeat(count) {
      decorations[index + it].onRemoved()
    }
    decorations.remove(index, count)
  }

  override fun move(from: Int, to: Int, count: Int) {
    decorations.move(from, to, count)
  }

  override fun onClear() {
    decorations.forEach { it.onCleared() }
    decorations.clear()
  }
}