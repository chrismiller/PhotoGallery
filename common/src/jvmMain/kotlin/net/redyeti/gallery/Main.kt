package net.redyeti.gallery

import kotlinx.coroutines.runBlocking
import net.redyeti.gallery.di.initKoin
import net.redyeti.gallery.remote.PhotoGalleryApi

fun main() {
    runBlocking {
        val koin = initKoin(enableNetworkLogs = true).koin
        val api = koin.get<PhotoGalleryApi>()
        println(api.fetchAlbums())
    }
}
