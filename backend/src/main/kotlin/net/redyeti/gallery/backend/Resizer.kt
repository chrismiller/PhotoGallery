package net.redyeti.gallery.backend

import java.nio.file.Path

class Resizer(val imageMagick: Path) {
  fun resize(source: Path, target: Path, maxTargetDimension: Int): Int {
    return apply(source, target, maxTargetDimension, "-resize")
  }

  fun thumbnail(source: Path, target: Path, maxTargetDimension: Int): Int {
    return apply(source, target, maxTargetDimension, "-thumbnail")
  }

  private fun apply(source: Path, target: Path, maxTargetDimension: Int, command: String): Int {
    return execute(listOf(imageMagick.toString(), source.toString(), command,
      "${maxTargetDimension}x${maxTargetDimension}", target.toString()), {}, {}, source.parent)
  }
}