import androidx.compose.runtime.*
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.Album
import net.redyeti.gallery.repository.PhotoGalleryInterface
import kotlinx.coroutines.InternalCoroutinesApi
import net.redyeti.gallery.remote.GpsCoordinates
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val koin = initKoin(enableNetworkLogs = true).koin

@InternalCoroutinesApi
fun main() {
    val repo = koin.get<PhotoGalleryInterface>()

    renderComposable(rootElementId = "root") {
        Style(TextStyles)

        var albums by remember { mutableStateOf(emptyList<Album>()) }

        LaunchedEffect(true) {
            albums = repo.fetchAlbums()
        }

        val gpsCoordinates by produceState(initialValue = GpsCoordinates(0.0, 0.0, 0.0), repo) {
            repo.pollISSPosition().collect { value = it }
        }

        Div(attrs = { style { padding(16.px) } }) {
            H1(attrs = { classes(TextStyles.titleText) }) {
                Text("Photo Gallery")
            }
            H2 {
                Text("Location: latitude = ${gpsCoordinates.latitude}, longitude = ${gpsCoordinates.longitude}")
            }

            H1 {
                Text("Albums")
            }
            albums.forEach { album ->
                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            alignItems(AlignItems.Center)
                        }
                    }
                ) {
                    Img(
                        src = "/image/${album.directory}/thumb/${album.coverImage}",
                        attrs = {
                            style {
                                width(48.px)
                                property("padding-right", 16.px)
                            }
                        }
                    )

                    Span(attrs = { classes(TextStyles.albumText) }) {
                        Text("${album.name} (${album.year})")
                    }
                }
            }
        }
    }
}

object TextStyles : StyleSheet() {

    val titleText by style {
        color(rgb(23,24, 28))
        fontSize(50.px)
        property("font-size", 50.px)
        property("letter-spacing", (-1.5).px)
        property("font-weight", 900)
        property("line-height", 58.px)

        property(
            "font-family",
            "Gotham SSm A,Gotham SSm B,system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
        )
    }

    val albumText by style {
        color(rgb(23,24, 28))
        fontSize(24.px)
        property("font-size", 28.px)
        property("letter-spacing", "normal")
        property("font-weight", 300)
        property("line-height", 40.px)

        property(
            "font-family",
            "Gotham SSm A,Gotham SSm B,system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
        )
    }
}
