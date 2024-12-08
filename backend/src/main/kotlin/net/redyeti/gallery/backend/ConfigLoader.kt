package net.redyeti.gallery.backend

import java.io.FileInputStream
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

class ConfigLoader {
  companion object {
    private const val MIN_LARGE_DIMENSION = "1600"
    private const val MIN_THUMBNAIL_DIMENSION = "200"
    private const val DEFAULT_PORT = "8081"
  }

  fun load(configFile: String): AppConfig {
    val props = Properties()
    FileInputStream(configFile).use {
      props.load(it)
    }

    // This allows us to mount as many arbitrary static directories as we like, based on entries starting
    // with "STATIC_DIR." in the config file. For example, STATIC_DIR.extra=D:/Photos/PhotoGallery/extra
    // will mount files under D:/Photos/PhotoGallery/extra as "/extra"
    val otherStatic = mutableMapOf<String, Path>()
    props.forEach { k, v ->
      if (k.toString().startsWith("STATIC_DIR.")) {
        val route = k.toString().substring("STATIC_DIR.".length)
        otherStatic["/$route"] = Path.of(v.toString())
      }
    }

    return AppConfig(
      Path(props.getProperty("EXIFTOOL", "exiftool.exe")),
      Path(props.getProperty("IMAGEMAGICK", "imagemagick.exe")),
      Path(props.getProperty("GALLERY_BASE", Path.of(configFile).parent.toString())),
      props.getProperty("HTTP_PORT", DEFAULT_PORT).toInt(),
      props.getProperty("MIN_LARGE_DIMENSION", MIN_LARGE_DIMENSION).toInt(),
      props.getProperty("MIN_THUMBNAIL_DIMENSION", MIN_THUMBNAIL_DIMENSION).toInt(),
      otherStatic
    )
  }
}

class AppConfig(
  val exiftool: Path,
  val imageMagick: Path,
  val galleryDir: Path,
  val port: Int,
  val minLargeDimension: Int,
  val minThumbDimension: Int,
  val otherStatic: Map<String, Path>
) {

  val staticImageDir get() = galleryDir.resolve("web")
  val staticGpsTrackDir get() = galleryDir.resolve("gpstracks")
  val metadataDir get() = galleryDir.resolve("metadata")
  val albumsFile get() = galleryDir.resolve("albums.csv")
  fun largeDir(albumDirectory: String) = staticImageDir.resolve(albumDirectory).resolve("large")
  fun thumbnailDir(albumDirectory: String) = staticImageDir.resolve(albumDirectory).resolve("thumb")
  fun originalsDir(albumDirectory: String) = galleryDir.resolve("originals").resolve(albumDirectory)
  fun gpsTrackDir(albumDirectory: String) = staticGpsTrackDir.resolve(albumDirectory)
}