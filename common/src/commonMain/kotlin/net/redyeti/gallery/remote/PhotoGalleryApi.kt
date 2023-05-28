package net.redyeti.gallery.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import kotlin.math.max

@Serializable
data class Album(
  val id: Int, val title: String, val subtitle: String, val directory: String, val coverImage: String
) {
  fun imageUrl(photo: Photo) = imageUrl(photo.filename)
  fun thumbnailUrl(photo: Photo) = thumbnailUrl(photo.filename)
  fun imageUrl(filename: String) = "/image/$directory/large/$filename"
  fun thumbnailUrl(filename: String) = "/image/$directory/thumb/$filename"
}

@Serializable
data class Photo(
  val id: Int, val filename: String, val description: String, val width: Int, val height: Int, val timeTaken: String,
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
data class GpsCoordinates(val latitude: Double, val longitude: Double, val altitude: Double)

class PhotoGalleryApi(private val client: HttpClient, var baseUrl: String = "http://localhost:8081/api") :
  KoinComponent {
  suspend fun fetchAlbums(): List<Album> {
    return client.get("$baseUrl/albums").body<List<Album>>()
  }

  suspend fun fetchAlbum(id: Int) = client.get("$baseUrl/album/$id").body<PopulatedAlbum>()
  suspend fun fetchPhoto(id: Int) = client.get("$baseUrl/photo/$id").body<Photo>()
}
