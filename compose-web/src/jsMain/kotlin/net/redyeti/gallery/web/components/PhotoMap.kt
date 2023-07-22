package net.redyeti.gallery.web.components

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import com.chihsuanwu.maps.compose.web.*
import com.chihsuanwu.maps.compose.web.drawing.Marker
import com.chihsuanwu.maps.compose.web.drawing.MarkerIcon
import com.chihsuanwu.maps.compose.web.drawing.MarkerState
import com.chihsuanwu.maps.compose.web.layers.KMLLayer
import kotlinx.browser.window
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.remote.Photo
import net.redyeti.gallery.remote.PopulatedAlbum
import net.redyeti.gallery.web.map.getMapRegion
import net.redyeti.gallery.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.math.min

private class State(album: PopulatedAlbum, mapWidth: Double, mapHeight: Double) {
  val photos = album.photos
  val mapRegion = getMapRegion(photos.mapNotNull { it.location }, mapWidth, mapHeight)
  val cameraPositionState = CameraPositionState(
    CameraPosition(
      center = mapRegion.centre, zoom = mapRegion.zoomLevel
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
  val mapWidth = window.innerWidth * 0.8
  val mapHeight = window.innerHeight * 0.7
  val state = State(album, mapWidth, mapHeight)

  val mapOptions by remember {
    mutableStateOf(
      MapOptions(
        fullscreenControl = true,
        isFractionalZoomEnabled = true
      )
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
    if (album.album.hasGpsTrack) {
      val url = "https://www.redyeti.net${album.album.kmlUrl()}"
      console.info(url)
      KMLLayer(url = url)
    }

    val router = Router.current
    state.markers.forEach {
      val (photo, marker) = it
      val width = min(markerPhotoSize, photo.width * markerPhotoSize / photo.height)
      val height = min(markerPhotoSize, photo.height * markerPhotoSize / photo.width)
      Marker(state = marker,
        title = photo.filename,
        icon = MarkerIcon.Icon(
          url = album.thumbnailUrl(photo.id),
          scaledSize = Size(height.toDouble(), width.toDouble())
        ),
        onClick = {
          state.selectedMarker?.hideInfoWindow()
          state.selectedMarker = marker
          state.selectedPhoto = photo
          marker.showInfoWindow()
        },
        infoContent = {
          NavOnlyLink(to = "/map/${album.album.id}/${photo.id}", router = router) {
            MapInfoWindow(album.album, photo)
          }
        })
    }
  }
}

@Composable
fun MapInfoWindow(album: Album, photo: Photo) {
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
