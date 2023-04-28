package net.redyeti.gallery.backend

import java.nio.file.Path

class Resizer(val imageMagick: Path) {
  fun resize(source: Path, target: Path, minTargetDimension: Int): Int {
    // Slow but high quality resizing
    //
    // Other possible unsharp values to try:
    //   -unsharp 1.1x1+0.6+0.02 - this one's often a bit over-sharpened
    //   10x4+1+0
    //   1.0x1.0+0.5+0.1
    return execute(listOf(
      imageMagick.toString(), "convert", source.toString(), "-filter", "Lanczos", "-sampling-factor",
      "1x1", "-gamma", "0.4545455", "-resize", "${minTargetDimension}x${minTargetDimension}^",
      "-unsharp", "1.1x1+0.5+0.008", "-quality", "75", "-gamma", "2.2", target.toString()
    ), {}, {}, source.parent
    )
  }
}