package net.redyeti.gallery.repository

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
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

  val coroutineScope: CoroutineScope = MainScope()

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

  companion object {
    private const val POLL_INTERVAL = 1_000L
  }
}
