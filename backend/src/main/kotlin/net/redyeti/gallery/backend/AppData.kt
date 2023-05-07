package net.redyeti.gallery.backend

import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum

class AppData(albums: List<PopulatedAlbum>) {
  private val albums = albums.map { it.album }
  private val albumsById = albums.associateBy { it.album.id }
  private val photosByAlbumAndId = albumsById.mapValues { it.value.photos.associateBy { photo -> photo.id } }

  fun getAlbums(): List<Album> = albums

  fun getAlbum(id: Int): PopulatedAlbum? = albumsById[id]

  fun getPhoto(albumId: Int, photoId: Int): Photo? = photosByAlbumAndId[albumId]?.get(photoId)
}
