package net.redyeti.gallery.backend.algeria

import net.redyeti.gallery.remote.Photo

private val utmRegex = "(GPS|NABSQNO)\\s+(\\d\\d[A-Z])\\s+(\\d+)\\s+(\\d+)".toRegex()

private val archNameRegex = "ALG-?\\d+".toRegex()

private fun insertArchLinks(photo: Photo, archMap: Map<String, Photo>): String {
  var thisName = photo.filename.substringBefore('.')
  val description = archNameRegex.replace(photo.description) { m ->
    val name = m.value.replace("-", "")
    if (name != thisName) {
      archMap[name]?.let { photo ->
        return@replace "{photo:${photo.id}}${m.value}{/}"
      }
    }
    // There's no link, so leave as-is
    return@replace m.value
  }
  return description
}

private fun insertMapLinks(description: String): String {
  val description = utmRegex.replace(description) { m ->
    val part1 = m.groups[2]!!.value
    val part2 = m.groups[3]!!.value
    val part3 = m.groups[4]!!.value
    val utm = parseUtm("$part1 $part2 $part3")
    val latLon = utm.convertToLatLon()
    return@replace "{map:${latLon.lat},${latLon.lon}}${m.value}{/}"
  }
  return description
}

fun algeriaTransform(photos: List<Photo>): List<Photo> {
  // Create a map of arch name to photo
  val archNameMap = photos.associateBy { photo ->
    photo.filename.substringBefore('.')
  }
  // Now look for arch names in the descriptions, and create link markup if we find any
  return photos.map { photo ->
    var description = insertArchLinks(photo, archNameMap)
    description = insertMapLinks(description)

    if (description.length > photo.description.length) {
      return@map photo.copy(description = description)
    }
    return@map photo
  }
}