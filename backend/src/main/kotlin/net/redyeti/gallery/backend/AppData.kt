package net.redyeti.gallery.backend

import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import java.nio.file.Path

class AppData(albums: List<PopulatedAlbum>) {
  private val albums = albums.map { it.album }
  private val albumsById = albums.associateBy { it.album.id }
  private val photosById = albums.flatMap { it.photos }.associateBy { it.id }

  fun getAlbums(): List<Album> = albums

  fun getAlbum(id: Int): PopulatedAlbum? = albumsById[id]

  fun getPhoto(id: Int): Photo? = photosById[id]
}

class AppConfig(val exiftool: Path, val imageMagick: Path, val baseAlbumDir: Path, val port: Int)