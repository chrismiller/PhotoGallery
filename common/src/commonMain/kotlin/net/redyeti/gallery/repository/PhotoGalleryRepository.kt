package net.redyeti.gallery.repository

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.PhotoGalleryApi
import net.redyeti.gallery.remote.PopulatedAlbum
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PhotoGalleryInterface {
  suspend fun fetchAlbums(): List<Album>

  suspend fun fetchAlbum(key: String): PopulatedAlbum
}

class PhotoGalleryRepository : KoinComponent, PhotoGalleryInterface {
  private val photoGalleryApi: PhotoGalleryApi by inject()
  private val coroutineScope: CoroutineScope by inject()

  val logger = Logger.withTag(this::class.simpleName!!)

  init {
    coroutineScope.launch {
      fetchAndStoreAlbums()
    }
  }

  suspend fun fetchAndStoreAlbums() {
    logger.d { "fetchAndStoreAlbums" }
  }

  // Used by web client
  override suspend fun fetchAlbums(): List<Album> {
    return photoGalleryApi.fetchAlbums()
  }

  // Used by web client
  override suspend fun fetchAlbum(key: String): PopulatedAlbum = photoGalleryApi.fetchAlbum(key)
}
