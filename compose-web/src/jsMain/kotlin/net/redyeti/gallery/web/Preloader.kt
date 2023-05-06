package net.redyeti.gallery.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.sync.Semaphore
import org.w3c.dom.Image

object Preloader {
  @Composable
  fun ImgPreload(url: String, onLoaded: () -> Unit = {}) {
    val signal = Semaphore(1, 1)
    LaunchedEffect(url) {
      val img = Image()
      img.onload = {
        signal.release()
      }
      // This will start the load
      img.src = url
      signal.acquire()
      onLoaded()
    }
  }
}