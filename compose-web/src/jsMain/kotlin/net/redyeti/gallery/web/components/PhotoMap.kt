package net.redyeti.gallery.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.chihsuanwu.maps.compose.web.*
import com.chihsuanwu.maps.compose.web.drawing.Marker
import com.chihsuanwu.maps.compose.web.drawing.MarkerState
import net.redyeti.gallery.remote.Photo
import org.jetbrains.compose.web.css.*

@Composable
fun PhotoMap(photos: List<Photo>) {
  val avgLat = photos.mapNotNull { it.location?.latitude }.average()
  val avgLong = photos.mapNotNull { it.location?.longitude }.average()
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition(
      center = LatLng(avgLat, avgLong),
      zoom = 8.0,
    )
  }

  val mapOptions = remember {
    MapOptions(
      fullscreenControl = true
    )
  }

  GoogleMap(
    apiKey = "AIzaSyDm41vgFLFbuCLCTIYTKZpQ9phGBRAPPc4",
    mapOptions = mapOptions,
    cameraPositionState = cameraPositionState,
    attrs = {
      style {
        width(600.px)
        height(400.px)
      }
    }
  ) {
    photos.forEach { p->
      val coords = p.location
      if (coords != null) {
        Marker(
          state = MarkerState(position = LatLng(coords.latitude, coords.longitude)),
          onClick = {
          }
        )
      }
    }
  }
}