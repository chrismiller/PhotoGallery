package net.redyeti.gallery.backend

import io.jenetics.jpx.GPX
import io.jenetics.jpx.GPX.Reader.Mode
import net.redyeti.gallery.remote.FeatureCollectionData
import net.redyeti.gallery.remote.FeatureData
import net.redyeti.gallery.remote.GeometryData
import java.io.BufferedInputStream
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.jvm.optionals.getOrDefault

fun loadGpsTracks(gpsTrackDirectory: Path): FeatureCollectionData {
  val tracks = mutableListOf<FeatureData>()
  gpsTrackDirectory.listDirectoryEntries()
    .filter { it.isRegularFile() && it.extension == "gpx" }
    .forEach { gpxFile -> tracks += loadGpxFile(gpxFile) }
  return FeatureCollectionData("FeatureCollection", tracks)
}

fun loadGpxFile(gpxFile: Path): List<FeatureData> {
  println("Loading gpx file $gpxFile")
  val features = mutableListOf<FeatureData>()
  try {
    val stream = gpxFile.inputStream().skipBOM()
    GPX.Reader.of(Mode.LENIENT).read(stream).tracks().forEach { track ->
      val coords = mutableListOf<DoubleArray>()
      track.segments().forEach { segment ->
        segment.points.forEach { point ->
          coords += doubleArrayOf(
            point.longitude.toDouble(),
            point.latitude.toDouble(),
            point.elevation.getOrDefault(0.0).toDouble()
          )
        }
      }
      if (coords.isNotEmpty()) {
        val geometry = GeometryData("LineString", coords)
        features += FeatureData("Feature", geometry)
      }
    }
  } catch (e: Exception) {
    println("Failed loading gpx file: $e")
  }
  return features
}

fun InputStream.skipBOM(): InputStream = BufferedInputStream(this).apply {
  mark(3)
  val bytes = ByteArray(3)
  if (read(bytes, 0, 3) != 3 || bytes[0] != 0xEF.toByte() || bytes[1] != 0xBB.toByte() || bytes[2] != 0xBF.toByte()) {
    reset()
  }
}