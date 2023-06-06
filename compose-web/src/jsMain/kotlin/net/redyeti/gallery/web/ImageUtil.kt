package net.redyeti.gallery.web

fun sizedSVG(width: Int, height: Int): String {
  return "data:image/svg+xml,%3Csvg xmlns=\"http://www.w3.org/2000/svg\" width=\"$width\" height=\"$height\" viewBox=\"0 0 $width $height\"%3E%3C/svg%3E"
}