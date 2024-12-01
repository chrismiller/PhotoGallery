package net.redyeti.gallery.backend

import net.redyeti.gallery.remote.Photo

private val urlRegex = "(https?://.*)\\s".toRegex()

private fun insertUrlLinks(photo: Photo): String {
  val description = urlRegex.replace(photo.description) { m ->
    val url = m.groupValues[1]
    return@replace "{url:$url}$url{/}"
  }
  return description
}

fun insertUrlLinks(photos: List<Photo>): List<Photo> {
  return photos.map { photo ->
    val description = insertUrlLinks(photo)
    if (description.length > photo.description.length) {
      return@map photo.copy(description = description)
    }
    return@map photo
  }
}
