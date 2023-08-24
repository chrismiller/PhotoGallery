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
}
