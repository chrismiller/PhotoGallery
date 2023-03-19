package net.redyeti.gallery.repository

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import net.redyeti.gallery.di.PhotoGalleryDatabaseWrapper
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.GpsCoordinates
import net.redyeti.gallery.remote.PhotoGalleryApi
import net.redyeti.gallery.remote.PopulatedAlbum
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PhotoGalleryInterface {
  @NativeCoroutines
  fun fetchAlbumsAsFlow(): Flow<List<Album>>

  @NativeCoroutines
  fun pollISSPosition(): Flow<GpsCoordinates>

  @NativeCoroutines
  suspend fun fetchAlbums(): List<Album>

  @NativeCoroutines
  suspend fun fetchAlbum(id: Int): PopulatedAlbum
  suspend fun fetchAndStoreAlbums()
}

class PhotoGalleryRepository : KoinComponent, PhotoGalleryInterface {
  private val photoGalleryApi: PhotoGalleryApi by inject()

  @NativeCoroutineScope
  val coroutineScope: CoroutineScope = MainScope()
  private val photoGalleryDatabase: PhotoGalleryDatabaseWrapper by inject()
  private val photoGalleryQueries = photoGalleryDatabase.instance?.photoGalleryQueries

  val logger = Logger.withTag(this::class.simpleName!!)

  init {
    coroutineScope.launch {
      fetchAndStoreAlbums()
    }
  }

  @NativeCoroutines
  override fun fetchAlbumsAsFlow(): Flow<List<Album>> {
    // the main reason we need to do this check is that sqldelight isn't currently
    // setup for javascript client
    return photoGalleryQueries?.selectAll(
      mapper = { id, name, year, month, directory, coverImage ->
        Album(id, name, year, month, directory, coverImage)
      }
    )?.asFlow()?.mapToList() ?: flowOf(emptyList())
  }

  override suspend fun fetchAndStoreAlbums() {
    logger.d { "fetchAndStoreAlbums" }
    try {
      val result = photoGalleryApi.fetchAlbums()

      // this is very basic implementation for now that removes all existing rows
      // in db and then inserts results from api request
      // using "transaction" accelerate the batch of queries, especially inserting
      photoGalleryQueries?.transaction {
        photoGalleryQueries.deleteAllAlbums()
        result.forEach {
          photoGalleryQueries.insertAlbum(
            it.id,
            it.name,
            it.year,
            it.month,
            it.directory,
            it.coverImage
          )
        }
      }
    } catch (e: Exception) {
      // TODO report error up to UI
      logger.w(e) { "Exception during fetchAndStoreAlbums: $e" }
    }
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
