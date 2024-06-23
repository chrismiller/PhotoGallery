package net.redyeti.gallery.backend

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.e175.klaus.solarpositioning.DeltaT
import net.e175.klaus.solarpositioning.SPA
import net.e175.klaus.solarpositioning.SunriseResult
import net.redyeti.gallery.remote.*
import net.redyeti.util.CsvParser
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.*
import kotlin.math.max
import kotlin.math.roundToInt

class AlbumScanner(val config: AppConfig) {
  companion object {
    val logger = Logger.withTag(this::class.simpleName!!)

    private val EXIFTOOL_METADATA_SCAN = listOf(
      // The # suffix returns the value without any automatic formatting/display conversion
      "-S", "-csv", "-FileName", "-FileSize#", "-DateTimeOriginal", "-OffsetTimeOriginal",
      "-ImageWidth", "-ImageHeight", "-GPSLatitude#", "-GPSLongitude#", "-GPSAltitude#",
      "-Model", "-ApertureValue", "-ExposureTime", "-FocalLength", "-ISO#", "-Lens", "-Description",
      "*.jpg"
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
    val headers = mutableListOf<String>()
    val parser = CsvParser()
    config.albumsFile.forEachLine {
      if (it.isBlank() || it.startsWith("//")) {
        // Ignore blank lines and comments
        return@forEachLine
      }
      if (headers.isEmpty()) {
        headers.addAll(parser.parseLine(it.reader()))
        return@forEachLine
      }
      val row = parser.parseLine(it.reader(), headers)
      val title = row["title"]
      val subtitle = row["subtitle"]
      var directory = row["directory"]
      val coverImage = row["cover"]

      if (title != null && subtitle != null && directory != null && coverImage != null) {
        val hidden = directory.startsWith('|')
        directory = directory.substringAfter('|')

        // Take note of the restrictions on KML files here: https://developers.google.com/maps/documentation/javascript/kmllayer#restrictions
        val hasGpsTrack = config.gpsTrackDir(directory).exists()
        try {
          val photos = loadAlbum(directory)
          val coverPhoto = photos.first { photo -> photo.filename == coverImage }
          val album = Album(directory, title, subtitle, coverPhoto, hasGpsTrack, hidden)
          albums += PopulatedAlbum(album, photos)
          logger.i("Loaded ${album.title}")
        } catch (e: Exception) {
          logger.e("Error loading album '${directory}', skipping this album. Reason: ${e.message}", e)
        }
      }
    }
    // Reverse the order so the newest albums are shown first
    albums.reverse()
    return albums
  }

  fun loadAlbum(albumKey: String): List<Photo> {
    // If a metadata file exists we just use that, otherwise scan and resize+thumbnail the whole album
    return loadAlbumMetadata(albumKey) ?: scanAndProcessAlbum(albumKey)
  }

  fun scanAndProcessAlbum(albumKey: String): List<Photo> {
    val originalsDir = config.originalsDir(albumKey)

    // Run exiftool to get all the photo metadata
    val params = listOf(config.exiftool.toString()) + EXIFTOOL_METADATA_SCAN
    val parser = CsvParser()
    val headers = mutableListOf<String>()

    class RowCollector {
      private var fullRow = ""
      private var quoteCount = 0
      fun gather(s: String): Boolean {
        quoteCount += s.count { it == '"' }
        fullRow = if (fullRow.isEmpty()) s else "$fullRow\n$s"
        return quoteCount % 2 == 0
      }

      fun take(): String {
        return fullRow.also {
          fullRow = ""
          quoteCount = 0
        }
      }
    }

    val pattern = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss z")
    var prevTimeOffset = "UTC"

    val photos = mutableListOf<Photo>()
    val collector = RowCollector()
    val processor: (String) -> Unit = { s ->
      if (collector.gather(s)) {
        val csvRow = collector.take()
        println(csvRow)
        if (headers.isEmpty()) {
          headers += parser.parseLine(csvRow.reader())
        } else {
          val row = parser.parseLine(csvRow.reader(), headers)
          val filename = row["FileName"]!!
          val fileSize = row["FileSize#"].asInt()
          val timeTakenStr = row["DateTimeOriginal"]!!
          var timeOffset = row["OffsetTimeOriginal"]
          if (timeOffset.isNullOrEmpty()) {
            timeOffset = prevTimeOffset
            logger.w("No timezone found for $originalsDir\\$filename - using $timeOffset")
          }
          prevTimeOffset = timeOffset
          val timeTaken = try {
            ZonedDateTime.parse("$timeTakenStr $timeOffset", pattern)
          } catch (e: Exception) {
            throw IllegalArgumentException("Could not parse time for $originalsDir/$filename (${e.message})")
          }
          val description = row["Description"].orEmpty()
          val camera = row["Model"].orEmpty()
          val lens = row["Lens"].orEmpty()
          val aperture = row["ApertureValue"].orEmpty()
          val focalLength = row["FocalLength"].orEmpty()
          val shutterSpeed = row["ExposureTime"].orEmpty()
          val iso = row["ISO#"].asInt()
          val fullWidth = row["ImageWidth"].asInt()
          val fullHeight = row["ImageHeight"].asInt()
          val latitude = row["GPSLatitude#"].asDouble()
          val longitude = row["GPSLongitude#"].asDouble()
          val altitude = row["GPSAltitude#"].asDouble()
          val location = if (latitude != 0.0 || longitude != 0.0 || altitude != 0.0) {
            GpsCoordinates(latitude, longitude, altitude)
          } else {
            null
          }
          val sunDetails = if (latitude != 0.0 || longitude != 0.0) {
            val deltaT = DeltaT.estimate(timeTaken.toLocalDate())
            val res = SPA.calculateSunriseTransitSet(timeTaken, latitude, longitude, deltaT)
            val position = SPA.calculateSolarPosition(timeTaken, latitude, longitude, altitude, deltaT, 1010.0, 20.0)
            when (res) {
              is SunriseResult.AllDay -> {
                SunDetails(DayType.NoSunset, 0, 0, 0, position.azimuth, position.zenithAngle)
              }

              is SunriseResult.AllNight -> {
                SunDetails(DayType.NoSunrise, 0, 0, 0, position.azimuth, position.zenithAngle)
              }

              is SunriseResult.RegularDay -> {
                SunDetails(
                  DayType.Normal,
                  res.sunrise.toEpochSecond(),
                  res.sunset.toEpochSecond(),
                  res.transit.toEpochSecond(),
                  position.azimuth,
                  position.zenithAngle
                )
              }
            }
          } else {
            null
          }

          // TODO: it's possible this suffers from off-by-one rounding issues compared to the actual sizes.
          //  Not sure if that matters or not, but if so we might have to scan the resized photo metadata instead.
          val w = max(config.minLargeDimension, fullWidth * config.minLargeDimension / fullHeight)
          val h = max(config.minLargeDimension, fullHeight * config.minLargeDimension / fullWidth)

          val cameraDetails = CameraDetails(camera, lens, aperture, shutterSpeed, focalLength, iso)
          val photo = Photo(
            -1, albumKey, filename, description, w, h, fullWidth, fullHeight, fileSize,
            timeTaken.toEpochSecond(), timeOffset, location, sunDetails, cameraDetails
          )
          photos += photo
        }
      }
    }
    val exitCode = execute(params, processor, {}, originalsDir)
    if (exitCode != 0) {
      throw Exception("Failed to process $originalsDir: $exitCode")
    }

    photos.sortBy { it.epochSeconds }
    val photosWithIDs = photos.mapIndexed { id, photo -> photo.copy(id = id) }

    resizeAndStripExif(albumKey, photosWithIDs)
    savePhotoMetadata(albumKey, photosWithIDs)
    return photosWithIDs
  }

  private fun metadataFile(filename: String) = config.metadataDir.resolve("$filename.json")

  @OptIn(ExperimentalSerializationApi::class)
  private fun savePhotoMetadata(albumDir: String, photos: List<Photo>) {
    metadataFile(albumDir).outputStream().use {
      Json.encodeToStream(photos, it)
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  private fun loadAlbumMetadata(albumKey: String): List<Photo>? {
    val metadataFile = metadataFile(albumKey)
    if (metadataFile.notExists()) {
      return null
    }
    metadataFile.inputStream().use {
      return Json.decodeFromStream<List<Photo>>(it)
    }
  }

  private fun resizeAndStripExif(albumKey: String, photos: List<Photo>) {
    val resizer = Resizer(config.imageMagick)
    val dirName = albumKey
    val originalsDir = config.originalsDir(dirName)
    val largeDir = config.largeDir(dirName)
    val thumbnailDir = config.thumbnailDir(dirName)

    largeDir.createDirectories()
    thumbnailDir.createDirectories()

    runBlocking {
      val createLarge = launch(Dispatchers.Default) {
        // Create large versions of the images if they don't already exist
        photos.forEach {
          val source = originalsDir.resolve(it.filename)
          val large = largeDir.resolve(it.filename)
          if (large.notExists()) {
            launch {
              resizer.resize(source, large, config.minLargeDimension)
            }
          }
        }
      }
      createLarge.join()

      // Strip out as much EXIF data as we can
      val params = listOf(config.exiftool.toString()) + EXIFTOOL_STRIP_EXIF
      execute(params, {}, {}, largeDir)

      val createThumbnails = launch(Dispatchers.Default) {
        // Now create thumbnails from the large versions
        photos.forEach {
          val large = largeDir.resolve(it.filename)
          val thumbnail = thumbnailDir.resolve(it.filename)
          if (thumbnail.notExists()) {
            launch {
              resizer.resize(large, thumbnail, config.minThumbDimension)
            }
          }
        }
      }
      createThumbnails.join()
    }
  }
}

fun String?.asInt() = if (this.isNullOrEmpty()) 0 else toDouble().roundToInt()

fun String?.asDouble() = if (this.isNullOrEmpty()) 0.0 else toDouble()