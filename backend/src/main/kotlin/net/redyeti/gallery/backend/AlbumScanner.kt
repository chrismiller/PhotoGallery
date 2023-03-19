package net.redyeti.gallery.backend

import co.touchlab.kermit.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.util.CsvParser
import kotlin.io.path.*

class AlbumScanner(val config: AppConfig) {
  companion object {
    val logger = Logger.withTag(this::class.simpleName!!)

    private const val ALBUMS_CSV = "albums.csv"
    private const val ORIGINAL_PATH = "original"
    private const val LARGE_PATH = "large"
    private const val THUMBNAILS_PATH = "thumb"
    private const val MAX_LARGE_DIMENSION = 2048
    private const val MAX_THUMBNAIL_DIMENSION = 256

    private val EXIFTOOL_METADATA_SCAN = listOf(
      "-S", "-csv", "-FileName", "-DateTimeOriginal",
      "-OffsetTimeOriginal", "-ImageWidth", "-ImageHeight", "-GPSLatitude#", "-GPSLongitude#", "-GPSAltitude#",
      "-Description", "*.jpg"
    )

    private val EXIFTOOL_STRIP_EXIF = listOf(
      "-P", "-all=", "-tagsFromFile", "@",
      "-ColorSpace", "-InteropIndex", "-ICC_Profile",
      "-Artist", "-Copyright", "-CopyrightNotice", "-Creator", "-Rights", "-CreatorWorkEmail",
      "-time:all", "-location:all", "-GPSLatitude", "-GPSLongitude", "-GPSAltitude",
      "-overwrite_original", "*.*"
    )
  }

  fun loadAlbums(): List<PopulatedAlbum> {
    val albums = mutableListOf<PopulatedAlbum>()
    var albumId = 0

    val headers = mutableListOf<String>()
    val parser = CsvParser()
    config.baseAlbumDir.resolve(ALBUMS_CSV).forEachLine {
      if (headers.isEmpty()) {
        headers.addAll(parser.parseLine(it.reader()))
        return@forEachLine
      }
      val row = parser.parseLine(it.reader(), headers)
      val id = albumId++
      val name = row["name"]
      val year = row["year"].asInt()
      val month = row["month"].asInt()
      val directory = row["directory"]
      val coverImage = row["cover"]
      if (name != null && directory != null && coverImage != null) {
        val album = Album(id, name, year, month, directory, coverImage)
        println(album)
        try {
          albums += loadAlbum(album)
          logger.i("Loaded ${album.name}")
        } catch (e: Exception) {
          logger.e("Error loading album '${album.name}', skipping...'", e)
        }
      }
    }
    return albums
  }

  fun loadAlbum(album: Album): PopulatedAlbum {
    // If a metadata file exists we just use that, otherwise scan and resize+thumbnail the whole album
    return loadAlbumMetadata(album.directory) ?: scanAndProcessAlbum(album)
  }

  fun scanAndProcessAlbum(album: Album): PopulatedAlbum {
    var photoId = 0

    val albumDir = config.baseAlbumDir.resolve(album.directory)
    val originalsDir = albumDir.resolve(ORIGINAL_PATH)

    val photos = mutableListOf<Photo>()

    // Run exiftool to get all the photo metadata
    val params = listOf(config.exiftool.toString()) + EXIFTOOL_METADATA_SCAN
    val parser = CsvParser()
    val headers = mutableListOf<String>()
    val processor: (String) -> Unit = { s ->
      print(s)
      val fields = parser.parseLine(s.reader())
      if (headers.isEmpty()) {
        headers += fields
      } else {
        val row = parser.parseLine(s.reader(), headers)
        val filename = row["FileName"]!!
        val timeTaken = row["DateTimeOriginal"]
        val timeOffset = row["OffsetTimeOriginal"]
        val timeStr = if (timeTaken != null && timeOffset != null) "$timeTaken $timeOffset" else timeTaken!!
        val description = row["Description"] ?: ""
        val width = row["ImageWidth"].asInt()
        val height = row["ImageHeight"].asInt()
        val latitude = row["GPSLatitude#"].asDouble()
        val longitude = row["GPSLongitude#"].asDouble()
        val altitude = row["GPSAltitude#"].asDouble()
        val location = if (latitude != 0.0 || longitude != 0.0 || altitude != 0.0) {
          GpsCoordinates(latitude, longitude, altitude)
        } else {
          null
        }
        val photo = Photo(photoId++, filename, description, width, height, timeStr, location)
        println(photo)
        photos += photo
      }
    }
    val exitCode = execute(params, processor, {}, originalsDir)

    val populatedAlbum = PopulatedAlbum(album, photos)
    resizeAndStripExif(populatedAlbum)
    saveAlbumMetadata(populatedAlbum)
    return populatedAlbum
  }

  private fun metadataFile(albumDir: String) = config.baseAlbumDir.resolve(albumDir).resolve("metadata.json")

  @OptIn(ExperimentalSerializationApi::class)
  private fun saveAlbumMetadata(album: PopulatedAlbum) {
    metadataFile(album.album.directory).outputStream().use {
      Json.encodeToStream(album, it)
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  private fun loadAlbumMetadata(albumDir: String): PopulatedAlbum? {
    val metadataFile = metadataFile(albumDir)
    if (metadataFile.notExists()) {
      return null
    }
    metadataFile.inputStream().use {
      return Json.decodeFromStream<PopulatedAlbum>(it)
    }
  }

  private fun resizeAndStripExif(album: PopulatedAlbum) {
    val resizer = Resizer(config.imageMagick)
    val albumDir = config.baseAlbumDir.resolve(album.album.directory)
    val originalsDir = albumDir.resolve(ORIGINAL_PATH)
    val largeDir = albumDir.resolve(LARGE_PATH)
    val thumbnailDir = albumDir.resolve(THUMBNAILS_PATH)

    largeDir.createDirectories()
    thumbnailDir.createDirectories()

    // Create large versions of the images if they don't already exist
    album.photos.forEach {
      val source = originalsDir.resolve(it.filename)
      val large = largeDir.resolve(it.filename)
      if (large.notExists()) {
        resizer.resize(source, large, MAX_LARGE_DIMENSION)
      }
    }

    // Strip out as much EXIF data as we can
    val params = listOf(config.exiftool.toString()) + EXIFTOOL_STRIP_EXIF
    execute(params, {}, {}, largeDir)

    // Now create thumbnails from the large versions
    album.photos.forEach {
      val large = largeDir.resolve(it.filename)
      val thumbnail = thumbnailDir.resolve(it.filename)
      if (thumbnail.notExists()) {
        resizer.thumbnail(large, thumbnail, MAX_THUMBNAIL_DIMENSION)
      }
    }
  }
}

fun String?.asInt() = if (this.isNullOrEmpty()) 0 else this.toInt()

fun String?.asDouble() = if (this.isNullOrEmpty()) 0.0 else this.toDouble()