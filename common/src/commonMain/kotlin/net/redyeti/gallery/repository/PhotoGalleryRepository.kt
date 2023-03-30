package net.redyeti.gallery.repository

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.remote.PhotoGalleryApi
import net.redyeti.gallery.remote.PopulatedAlbum
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PhotoGalleryInterface {
  @NativeCoroutines
  fun pollISSPosition(): Flow<GpsCoordinates>

  @NativeCoroutines
  suspend fun fetchAlbums(): List<Album>

  @NativeCoroutines
  suspend fun fetchAlbum(id: Int): PopulatedAlbum
}

class PhotoGalleryRepository : KoinComponent, PhotoGalleryInterface {
  private val photoGalleryApi: PhotoGalleryApi by inject()

  @NativeCoroutineScope
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
  @NativeCoroutines
  override suspend fun fetchAlbums(): List<Album> {
    return photoGalleryApi.fetchAlbums()
  }

  // Used by web client
  @NativeCoroutines
  override suspend fun fetchAlbum(id: Int): PopulatedAlbum = photoGalleryApi.fetchAlbum(id)

  @NativeCoroutines
  override fun pollISSPosition(): Flow<GpsCoordinates> {
    return flow {
      while (true) {
        val position = photoGalleryApi.fetchAlbum(1)
        emit(GpsCoordinates(0.0, 0.0, 0.0))
        logger.d { position.toString() }
        delay(POLL_INTERVAL)
      }
    }
  }

  companion object {
    private const val POLL_INTERVAL = 1_000L
  }
}
