# Photo Gallery

This is personal project I built, it's the code that runs my https://redyeti.net photo album website. While the code
and configuration is somewhat generic and likely reusable, it's not a goal of mine to make this a completely
customisable gallery application for general-purpose use. That means I might make functionality or breaking changes at
any point in time, and I'm probably not likely to tackle any bug or feature requests unless they affect me directly,
so please bear that in mind if you choose to deploy your own website based on this code. I'd be happy to consider any
pull requests that do improve things or make this project a more generic solution though.

![kotlin-version](https://img.shields.io/badge/kotlin-2.1.0-orange) The gallery is built with **Kotlin Multiplatform**,
using Compose HTML for the web frontend and a Ktor server for the backend.

### Configuration

1. Download and install [exiftool](https://exiftool.org) and [ImageMagick](https://imagemagick.org/script/index.php).
2. Create a base directory where you want to store the gallery's static files (any original, full sized and thumbnail 
   images, along with album metadata information).
3. Create a `config.properties` file in this directory that contains your configuration settings. An example of a
   config file is shown below; update the paths and values to match your directory locations and settings preferences:
   ```
   EXIFTOOL=C:/Apps/Tools/exiftool.exe
   IMAGEMAGICK=C:/Apps/Tools/ImageMagick/magick.exe
   GALLERY_BASE=C:/Data/Photos/PhotoGallery
   HTTP_PORT=81
   MIN_LARGE_DIMENSION=1600
   MIN_THUMBNAIL_DIMENSION=200
   ```
4. Set an environment variable called `GALLERY_CONFIG` that is the full path and filename of your config file.
5. Create an `originals` directory inside the base directory.
6. For each photo album you want in the gallery, create a separate subdirectory inside `originals` that contains 
   full resolution copies of the photos for that album.
7. Create a file called `albums.csv` in the base directory that looks as follows, with one row for each album:
   ```
   // The first non-comment line must be the header. This determines the column order of the CSV lines below.
   directory,title,subtitle,cover

   // Albums at the top of the list will be displayed at the bottom of the website. Hence you should add an album to
   // the bottom of this file if you want it to appear at the top of the website.
   FamilyHoliday2024,"Family Holiday","July 2024",IMG_9270.jpg
   Wedding2023,"Our Wedding","March 2023",IMG_3682.jpg
   ```

### Building
To run backend you can either run `./gradlew :backend:run` or run `Server.kt` directly. After doing that you should
then for example be able to open http://localhost:81 in a browser.

When the server starts, it will scan the `albums.csv` file for any new albums. If it finds any, it will automatically
generate large and thumbnail images for that album, as well as extract any metadata from the original image files.
Any captions or GPS location information found will be used to generate photo captions and maps on the gallery website.

Album metadata will be saved in the base directory under `metadata/<album name>.json`, so it can load quickly if the
server is restarted. If you want to regenerate albums (e.g. because you've added new images to an existing album),
delete the appropriate metadata file and restart the server.

### Compose HTML client

The Compose HTML client resides in the `compose-web` module and can be run by
invoking `./gradlew :compose-web:jsBrowserDevelopmentRun`

### Backend code

Using shadowJar plugin to create an "uber" jar as shown below.

`./gradlew :backend:shadowJar`

### Languages, libraries and tools used

* [Kotlin](https://kotlinlang.org/)
* [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
* [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
* [Ktor client library](https://github.com/ktorio/ktor)
* [Koin](https://github.com/InsertKoinIO/koin)
* [KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)
