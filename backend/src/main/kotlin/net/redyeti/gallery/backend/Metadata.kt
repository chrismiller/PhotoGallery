package net.redyeti.gallery.backend

sealed class Metadata(val raw: Boolean = false) {
  abstract val group: String
  sealed class XMP: Metadata(raw = false) {
    object DateTimeOriginal: XMP()
    object DateCreated: XMP()
    object Lens: XMP()
    object Description: XMP()
    override val group = "XMP"
  }
  sealed class Exif(raw: Boolean = false): Metadata(raw) {
    object DateTimeOriginal: Exif()
    object OffsetTimeOriginal: Exif()
    object OffsetTime: Exif()
    object OffsetTimeDigitized: Exif()
    object ExposureTime: Exif()
    object FocalLength: Exif()
    object ISO: Exif(true)
    object Model: Exif()
    object ApertureValue: Exif()
    override val group = "EXIF"
  }
  sealed class MakerNotes(raw: Boolean = false): Metadata(raw) {
    object TimeZone: MakerNotes()
    override val group = "MakerNotes"
  }
  sealed class IPTC(raw: Boolean = false): Metadata(raw) {
    object TimeCreated: IPTC()
    override val group = "IPTC"
  }
  sealed class File(raw: Boolean = false): Metadata(raw) {
    object FileName: File()
    object FileSize: File(true)
    object FileCreateDate: File()
    object ImageWidth: File()
    object ImageHeight: File()
    override val group = "File"
  }
  sealed class Composite(raw: Boolean = false): Metadata(raw) {
    object SubSecDateTimeOriginal: Composite()
    object SubSecCreateDate: Composite()
    object DigitalCreationDateTime: Composite()
    object DateTimeCreated: Composite()
    object GPSLatitude: Composite(true)
    object GPSLongitude: Composite(true)
    object GPSAltitude: Composite(true)
    override val group = "Composite"
  }

  val name: String
    get() = if (raw) "${javaClass.simpleName}#" else javaClass.simpleName

  val fullName: String
    get() = "$group:$name"

  companion object {
    /**
     * Returns a map of fully qualified metadata field name to its matching class instance.
     * E.g. Exif:ISO# -> Metadata.Exif.ISO
     */
    val map: Map<String, Metadata> = Metadata::class.sealedSubclasses
      .flatMap { it.sealedSubclasses }
      .mapNotNull { t -> t.objectInstance }
      .associate { t -> t.fullName to t }

    fun allAsStrings(): Collection<String> = map.keys
  }
}
