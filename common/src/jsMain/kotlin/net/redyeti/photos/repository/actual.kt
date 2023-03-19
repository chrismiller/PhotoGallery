package net.redyeti.gallery.repository

import net.redyeti.gallery.di.PhotoGalleryDatabaseWrapper
import io.ktor.client.engine.js.*
import org.koin.dsl.module

actual fun platformModule() = module {
  single {
    PhotoGalleryDatabaseWrapper(null)
  }
  single { Js.create() }
}
