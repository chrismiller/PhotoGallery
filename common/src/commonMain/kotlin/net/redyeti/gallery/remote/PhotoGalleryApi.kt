package net.redyeti.gallery.remote

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

@Serializable
data class Album(
  val id: Int, val title: String, val subtitle: String, val directory: String, val coverImage: String
)

@Serializable
data class Photo(
  val id: Int, val filename: String, val description: String, val width: Int, val height: Int, val timeTaken: String,
  val location: GpsCoordinates?
)

@Serializable
data class PopulatedAlbum(val album: Album, val photos: List<Photo>)

@Serializable
data class GpsCoordinates(val latitude: Double, val longitude: Double, val altitude: Double)

class PhotoGalleryApi(private val client: HttpClient, var baseUrl: String = "http://localhost:8081/api") : KoinComponent {
  suspend fun fetchAlbums(): List<Album> {
    return client.get("$baseUrl/albums").body<List<Album>>()
  }

  suspend fun fetchAlbum(id: Int) = client.get("$baseUrl/album/$id").body<PopulatedAlbum>()
  suspend fun fetchPhoto(id: Int) = client.get("$baseUrl/photo/$id").body<Photo>()
}
