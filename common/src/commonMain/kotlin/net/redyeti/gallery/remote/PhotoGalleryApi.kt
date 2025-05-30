package net.redyeti.gallery.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import kotlin.math.abs
import kotlin.math.max

@Serializable
data class Album(
  val key: String,
  val title: String,
  val subtitle: String,
  val coverImage: Photo,
  val hasGpsTracks: Boolean,
  val hidden: Boolean,
) {
  val gpsUrl get() = "/api/gps/$key"
}

@Serializable
data class Photo(
  val id: Int, val directory: String, val filename: String, val description: String, val width: Int, val height: Int,
  val originalWidth: Int, val originalHeight: Int, val originalSize: Int,
  val epochSeconds: Long, val timeOffset: String, val location: GpsCoordinates?,
  val sunDetails: SunDetails?, val cameraDetails: CameraDetails
) {
  val imageUrl get() = "/image/$directory/large/$filename"

  val thumbnailUrl get() = "/image/$directory/thumb/$filename"

  fun scaledWidth(minDimension: Int): Int {
    return max(minDimension, width * minDimension / height)
  }

  fun scaledHeight(minDimension: Int): Int {
    return max(minDimension, height * minDimension / width)
  }
}

@Serializable
enum class DayType { Normal, NoSunset, NoSunrise }

@Serializable
data class SunDetails(
  val type: DayType, val sunriseEpoch: Long, val sunsetEpoch: Long, val transitEpoch: Long, val azimuth: Double, val zenithAngle: Double
)

@Serializable
data class CameraDetails(
  val camera: String, val lens: String,
  val aperture: String, val shutterSpeed: String,
  val focalLength: String, val iso: Int
)

@Serializable
data class PopulatedAlbum(val album: Album, val photos: List<Photo>) {
  /**
   * Wraps the ID photo around so it doesn't exceed the number of IDs in the album. This allows for easy
   * 'next' / 'previous' navigation.
   */
  fun wrappedID(id: Int) = (id + photos.size) % photos.size

  fun imageUrl(photoID: Int): String {
    val photo = photos[wrappedID(photoID)]
    return photo.imageUrl
  }

  fun thumbnailUrl(photoID: Int): String {
    val photo = photos[wrappedID(photoID)]
    return photo.thumbnailUrl
  }
}

@Serializable
data class GpsCoordinates(val latitude: Double, val longitude: Double, val altitude: Double = 0.0) {
  val latDMS get() = toDMS(latitude, if (latitude >= 0) 'N' else 'S')
  val longDMS get() = toDMS(longitude, if (longitude >= 0) 'E' else 'W')

  fun hasCoordinates() = latitude != 0.0 && longitude != 0.0

  val googleMapsUrl get() = "https://maps.google.com/maps?z=16&q=$latitude,$longitude&ll=$latitude,$longitude"

  private fun toDMS(value: Double, suffix: Char): String {
    val v = abs(value)
    val degrees = v.toInt()
    val minutesDecimal = (v - degrees) * 60
    val minutes = minutesDecimal.toInt()
    val secondsDecimal = (minutesDecimal - minutes) * 60
    val seconds = secondsDecimal.toInt()
    val fraction = ((secondsDecimal - seconds) * 10).toInt()
    return "$degrees° $minutes' $seconds.$fraction\"$suffix"
  }
}

@Serializable
data class GeometryData(val type: String, val coordinates: List<DoubleArray>)

@Serializable
data class FeatureData(val type: String, val geometry: GeometryData) //, val properties: Map<String, String>)

@Serializable
data class FeatureCollectionData(val type: String, val features: List<FeatureData>)

class PhotoGalleryApi(private val client: HttpClient, var baseUrl: String = "/api") :
  KoinComponent {
  suspend fun fetchAlbums(): List<Album> {
    return client.get("$baseUrl/albums").body<List<Album>>()
  }

  suspend fun fetchAlbum(key: String) = client.get("$baseUrl/album/$key").body<PopulatedAlbum>()
}
