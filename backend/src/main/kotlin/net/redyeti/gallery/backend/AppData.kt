package net.redyeti.gallery.backend

import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum

class AppData(albums: List<PopulatedAlbum>) {
  private val albums = albums.map { it.album }
  private val albumsByKey = albums.associateBy { it.album.key }
  private val photosByAlbumAndId = albumsByKey.mapValues { it.value.photos.associateBy { photo -> photo.id } }

  fun getAlbums(): List<Album> = albums

  fun getAlbum(key: String): PopulatedAlbum? = albumsByKey[key]

  fun getPhoto(albumKey: String, photoId: Int): Photo? = photosByAlbumAndId[albumKey]?.get(photoId)

  fun allAlbumsMerged(): PopulatedAlbum {
    // Create a new album that contains all photos, assigning new IDs as we go because the existing
    // ones overlap across albums. We mark the album as hidden, because it works well on map view
    // but brings the album view to its knees
    val allPhotos = mutableListOf<Photo>()
    var id = 0
    albums.forEach { album ->
      getAlbum(album.key)?.let {
        allPhotos += it.photos.map { it.copy(id = id++) }
      }
    }
    return PopulatedAlbum(Album("All", "All Photos", "Photos from all albums", albums[0].coverImage, false, true), allPhotos)
  }
}
