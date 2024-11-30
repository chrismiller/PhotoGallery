package net.redyeti.gallery.backend

import net.redyeti.gallery.backend.AlbumScanner.Companion.logger
import java.nio.file.Path
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Tries to determine the time a photo was taken (including timezone) from a photo's metadata.
 * The metadata attached to a photo varies a lot depending on the camera.
 * E.g. some files have missing OffsetTimeOriginal metadata, even though they have DateTimeOriginal.
 * > exiftool -filepath -if "not $SubSecDateTimeOriginal" -q -s3 -r .
 */
class TimeParser(val originalsDir: Path) {
  companion object {
    val pattern = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssz")
  }
  var prevTimeOffset = "UTC"

  fun getBestTime(row: Map<Metadata, String>): String {
    val fieldPriority = listOf(
      Metadata.Composite.SubSecDateTimeOriginal,
      Metadata.Composite.DigitalCreationDateTime,
      Metadata.Composite.SubSecCreateDate,
      Metadata.Composite.DateTimeCreated,
      Metadata.XMP.DateCreated,
      Metadata.Exif.DateTimeOriginal,
      Metadata.File.FileCreateDate      // Last resort when there's no metadata, not likely to be correct.
    )
    val timeLength = "2020:10:17 18:09:13".length
    for (f in fieldPriority) {
      val field = row[f]
      if (field != null && field.length >= timeLength) {
        // We don't care about sub-second and it's not always there, so it's easiest just to discard
        return field.substring(0, timeLength)
      }
    }
    return row[Metadata.File.FileCreateDate]!!
  }

  fun getBestTimeZone(row: Map<Metadata, String>): String {
    val fieldPriority = listOf(
      Metadata.Composite.SubSecDateTimeOriginal,
      Metadata.Composite.DigitalCreationDateTime,
      Metadata.Composite.SubSecCreateDate,
      Metadata.Composite.DateTimeCreated,
      Metadata.MakerNotes.TimeZone,
      Metadata.Exif.OffsetTimeOriginal,
      Metadata.Exif.OffsetTimeDigitized,
    )
    val offsetLength = "+01:00".length
    for (f in fieldPriority) {
      val field = row[f]
      if (field != null) {
        val i = field.indexOfAny(charArrayOf('-', '+'))
        if (i >= 0) {
          val timeOffset = field.substring(i, i + offsetLength)
          prevTimeOffset = timeOffset
          return timeOffset
        }
      }
    }
    logger.w("No timezone found for $originalsDir\\${row[Metadata.File.FileName]} - using $prevTimeOffset")
    return prevTimeOffset
  }

  fun timeTaken(row: Map<Metadata, String>): ZonedDateTime {
    val timeTakenStr = "${getBestTime(row)}${getBestTimeZone(row)}"
    try {
      return ZonedDateTime.parse(timeTakenStr, pattern)
    } catch (e: Exception) {
      throw IllegalArgumentException("Could not parse time '$timeTakenStr' for $originalsDir/${row[Metadata.File.FileName]} (${e.message})")
    }
  }
}