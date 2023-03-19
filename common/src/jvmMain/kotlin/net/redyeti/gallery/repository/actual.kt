package net.redyeti.gallery.repository

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import net.redyeti.gallery.di.PhotoGalleryDatabaseWrapper
import io.ktor.client.engine.java.*
import net.redyeti.gallery.db.PhotoGalleryDatabase
import org.koin.dsl.module

actual fun platformModule() = module {
  single {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
      .also { PhotoGalleryDatabase.Schema.create(it) }
    PhotoGalleryDatabaseWrapper(PhotoGalleryDatabase(driver))
  }
  single { Java.create() }
}
