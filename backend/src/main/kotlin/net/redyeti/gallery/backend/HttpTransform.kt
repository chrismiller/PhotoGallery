package net.redyeti.gallery.backend

import net.redyeti.gallery.remote.Photo

private val urlRegex = "((?:(?:https?|ftp)://)?[\\w/\\-?=%.]+\\.[\\w/\\-&?=%.]+)".toRegex()

internal fun insertUrlLinks(description: String): String {
  val result = urlRegex.replace(description) { m ->
    val url = m.groupValues[1]
    return@replace "{url:$url}$url{/}"
  }
  return result
}

fun insertUrlLinks(photos: List<Photo>): List<Photo> {
  return photos.map { photo ->
    val description = insertUrlLinks(photo.description)
    if (description.length > photo.description.length) {
      return@map photo.copy(description = description)
    }
    return@map photo
  }
}
