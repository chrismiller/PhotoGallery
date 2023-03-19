package net.redyeti.gallery

import net.redyeti.gallery.di.PhotoGalleryDatabaseWrapper
import net.redyeti.gallery.di.commonModule
import net.redyeti.gallery.repository.PhotoGalleryInterface
import net.redyeti.gallery.repository.platformModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PhotoGalleryTest: KoinTest {
    private val repo : PhotoGalleryInterface by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp()  {
        Dispatchers.setMain(StandardTestDispatcher())

        startKoin{
            modules(
                commonModule(true),
                platformModule(),
                module {
                    single { PhotoGalleryDatabaseWrapper(null) }
                }
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAlbums() = runTest {
        val result = repo.fetchAlbums()
        println(result)
        assertTrue(result.isNotEmpty())
    }
}
