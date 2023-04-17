package net.redyeti.gallery.backend

import java.nio.file.Path

class Resizer(val imageMagick: Path) {
  fun resize(source: Path, target: Path, minTargetDimension: Int): Int {
    return apply(source, target, minTargetDimension, "-resize")
  }

  fun thumbnail(source: Path, target: Path, minTargetDimension: Int): Int {
    return apply(source, target, minTargetDimension, "-thumbnail")
  }

  private fun apply(source: Path, target: Path, minTargetDimension: Int, command: String): Int {
    return execute(listOf(imageMagick.toString(), source.toString(), command,
      "${minTargetDimension}x${minTargetDimension}^", target.toString()), {}, {}, source.parent)
  }
}