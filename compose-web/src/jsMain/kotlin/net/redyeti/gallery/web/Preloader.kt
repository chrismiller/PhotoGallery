package net.redyeti.gallery.web

import org.w3c.dom.Image
import org.w3c.dom.events.Event

object Preloader {
  // A cache of preloaded images. We never look in this cache, it's just here to prevent
  // the browser from garbage collecting the most recent preloads.
  private const val IMG_CACHE_SIZE = 10
  private var imgCacheIndex = 0
  private val imgCache = arrayOfNulls<Image>(IMG_CACHE_SIZE)

  fun imgPreload(url: String, onLoaded: (Event) -> Unit = {}) {
    val img = Image()
    imgCache[imgCacheIndex] = img
    imgCacheIndex = (imgCacheIndex + 1) % IMG_CACHE_SIZE
    img.onload = { event ->
      onLoaded(event)
    }
    // This will start the load
    img.src = url
  }
}