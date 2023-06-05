package net.redyeti.gallery.backend

import java.io.FileInputStream
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

class ConfigLoader {
  companion object {
    private const val MIN_LARGE_DIMENSION = "1600"
    private const val MIN_THUMBNAIL_DIMENSION = "200"
  }

  fun load(): AppConfig {
    val configFile = System.getenv("REDYETI_CONFIG") ?: "E:/PhotoGallery/config.properties"

    val props = Properties()
    FileInputStream(configFile).use {
      props.load(it)
    }

    return AppConfig(
      Path(props.getProperty("EXIFTOOL", "exiftool.exe")),
      Path(props.getProperty("IMAGEMAGICK", "imagemagick.exe")),
      Path(props.getProperty("GALLERY_DIR", Path.of(configFile).parent.toString())),
      props.getProperty("HTTP_PORT", "8081").toInt(),
      props.getProperty("MIN_LARGE_DIMENSION", MIN_LARGE_DIMENSION).toInt(),
      props.getProperty("MIN_THUMBNAIL_DIMENSION", MIN_THUMBNAIL_DIMENSION).toInt()
    )
  }
}

class AppConfig(
  val exiftool: Path,
  val imageMagick: Path,
  val galleryDir: Path,
  val port: Int,
  val minLargeDimension: Int,
  val minThumbDimension: Int
) {

  val staticwebDir get() = galleryDir.resolve("web")
  val metadataDir get() = galleryDir.resolve("metadata")
  val albumsFile get() = galleryDir.resolve("albums.csv")
  fun largeDir(albumName: String) = staticwebDir.resolve(albumName).resolve("large")
  fun thumbnailDir(albumDirectory: String) = staticwebDir.resolve(albumDirectory).resolve("thumb")
  fun originalsDir(albumDirectory: String) = galleryDir.resolve("originals").resolve(albumDirectory)
}