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
  val id: Int,
  val title: String,
  val subtitle: String,
  val directory: String,
  val coverImage: String,
  val hasGpsTrack: Boolean
) {
  fun imageUrl(photo: Photo) = imageUrl(photo.filename)
  fun thumbnailUrl(photo: Photo) = thumbnailUrl(photo.filename)
  fun imageUrl(filename: String) = "/image/$directory/large/$filename"
  fun thumbnailUrl(filename: String) = "/image/$directory/thumb/$filename"
  fun kmlUrl() = "/gps/${directory}.kmz"
}

@Serializable
data class Photo(
  val id: Int, val filename: String, val description: String, val width: Int, val height: Int, val timeTaken: String,
  val aperture: String, val shutterSpeed: String, val focalLength: String, val iso: String, val lens: String,
  val location: GpsCoordinates?
) {
  fun scaledWidth(minDimension: Int): Int {
    return max(minDimension, width * minDimension / height)
  }

  fun scaledHeight(minDimension: Int): Int {
    return max(minDimension, height * minDimension / width)
  }
}

@Serializable
data class PopulatedAlbum(val album: Album, val photos: List<Photo>) {
  fun wrappedID(id: Int) = (id + photos.size) % photos.size

  fun imageUrl(photoID: Int): String {
    val photo = photos[wrappedID(photoID)]
    return album.imageUrl(photo)
  }

  fun thumbnailUrl(photoID: Int): String {
    val photo = photos[wrappedID(photoID)]
    return album.thumbnailUrl(photo)
  }
}

@Serializable
data class GpsCoordinates(val latitude: Double, val longitude: Double, val altitude: Double = 0.0) {
  val latDMS get() = toDMS(latitude, if (latitude >= 0) 'N' else 'S')
  val longDMS get() = toDMS(longitude, if (longitude >= 0) 'E' else 'W')

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

class PhotoGalleryApi(private val client: HttpClient, var baseUrl: String = "/api") :
  KoinComponent {
  suspend fun fetchAlbums(): List<Album> {
    return client.get("$baseUrl/albums").body<List<Album>>()
  }

  suspend fun fetchAlbum(id: Int) = client.get("$baseUrl/album/$id").body<PopulatedAlbum>()
  suspend fun fetchPhoto(id: Int) = client.get("$baseUrl/photo/$id").body<Photo>()
}
