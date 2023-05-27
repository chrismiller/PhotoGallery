package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import com.chihsuanwu.maps.compose.web.*
import com.chihsuanwu.maps.compose.web.drawing.Marker
import com.chihsuanwu.maps.compose.web.drawing.MarkerIcon
import com.chihsuanwu.maps.compose.web.drawing.MarkerState
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.math.min

private class State(album: PopulatedAlbum) {
  val photos = album.photos
  val avgLat = photos.mapNotNull { it.location?.latitude }.average()
  val avgLong = photos.mapNotNull { it.location?.longitude }.average()
  val cameraPositionState = CameraPositionState(
    CameraPosition(
      center = LatLng(avgLat, avgLong), zoom = 7.6
    )
  )
  var selectedPhoto: Photo? by mutableStateOf(null)
  var selectedMarker: MarkerState? by mutableStateOf(null)

  var markers by mutableStateOf(photos.mapNotNull { photo ->
    photo.location?.run {
      Pair(photo, MarkerState(LatLng(latitude, longitude)))
    }
  })
}

const val markerPhotoSize = 50

@Composable
fun PhotoMap(album: PopulatedAlbum) {
  val state = remember { State(album) }

  val mapOptions = remember {
    MapOptions(
      fullscreenControl = true
    )
  }

  GoogleMap(
    apiKey = "AIzaSyDm41vgFLFbuCLCTIYTKZpQ9phGBRAPPc4",
    mapOptions = mapOptions,
    cameraPositionState = state.cameraPositionState,
    onClick = {
      state.selectedMarker?.hideInfoWindow()
      state.selectedMarker = null
      state.selectedPhoto = null
    }
  ) {
    state.markers.forEach {
      val (photo, marker) = it
      val width = min(markerPhotoSize, photo.width * markerPhotoSize / photo.height)
      val height = min(markerPhotoSize, photo.height * markerPhotoSize / photo.width)
      Marker(state = marker,
        title = photo.filename,
        icon = MarkerIcon.Icon(
          url = album.imageUrl(photo.id),
          scaledSize = Size(height.toDouble(), width.toDouble())
        ),
        onClick = {
          console.info("Lat=${marker.position.lat}, ${photo.filename}")
          state.selectedMarker?.hideInfoWindow()
          state.selectedMarker = marker
          state.selectedPhoto = photo
          marker.showInfoWindow()
        },
        infoContent = {
          PhotoInfo(album.album, photo)
        })
    }
  }
}

@Composable
fun PhotoInfo(album: Album, photo: Photo) {
  Div(
    attrs = {
      style {
        backgroundColor(Color.white)
        padding(2.px)
        borderRadius(2.px)
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)
        width(photo.scaledWidth(200).px)
      }
    }
  ) {
    Img(
      attrs = {
        classes(AppStyle.thumb)
        style {
          width(100.percent)
          height(100.percent)
        }
      },
      src = album.thumbnailUrl(photo),
      alt = photo.description
    )
    Span({
      style {
        marginTop(4.px)
        color(Color.black)
        width(100.percent)
      }
    }) {
      Text(photo.description)
    }
  }
}
