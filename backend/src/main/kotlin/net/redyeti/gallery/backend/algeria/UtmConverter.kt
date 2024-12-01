package net.redyeti.gallery.backend.algeria

import kotlin.math.*

data class LatLonCoord(val lat: Double, val lon: Double) {
  override fun toString(): String {
    return "$lat $lon"
  }
}
data class UTMCoord(val zone: Int, val letter: Char, val easting: Double, val northing: Double) {
  override fun toString(): String {
    return "$zone$letter $easting $northing"
  }
}

/**
 * Parses a UTM string in the form "32R 543986 2709992"
 */
fun parseUtm(utmString: String): UTMCoord {
  val parts = utmString.split(" ")
  try {
    return UTMCoord(parts[0].substring(0, 2).toInt(), parts[0].elementAt(2), parts[1].toDouble(), parts[2].toDouble())
  } catch (e: Exception) {
    throw Exception("'$utmString' is an invalid UTM value. Must in the form '32R 543986 2709992'")
  }
}

fun LatLonCoord.convertToUTM(): UTMCoord {
  val zone: Int = floor(lon / 6.0 + 31.0).toInt()
  val letter: Char = if (lat < -72.0) 'C'
  else if (lat < -64.0) 'D'
  else if (lat < -56.0) 'E'
  else if (lat < -48.0) 'F'
  else if (lat < -40.0) 'G'
  else if (lat < -32.0) 'H'
  else if (lat < -24.0) 'J'
  else if (lat < -16.0) 'K'
  else if (lat < -8.0) 'L'
  else if (lat < 0.0) 'M'
  else if (lat < 8.0) 'N'
  else if (lat < 16.0) 'P'
  else if (lat < 24.0) 'Q'
  else if (lat < 32.0) 'R'
  else if (lat < 40.0) 'S'
  else if (lat < 48.0) 'T'
  else if (lat < 56.0) 'U'
  else if (lat < 64.0) 'V'
  else if (lat < 72.0) 'W'
  else 'X'

  var easting: Double = 0.5 * ln(
    (1.0 + cos(lat * Math.PI / 180.0) * sin(lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0)) / (1.0 - cos(lat * Math.PI / 180.0) * sin(
      lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0
    ))
  ) * 0.9996 * 6399593.62 / (1 + 0.0820944379.pow(2.0) * cos(lat * Math.PI / 180.0).pow(2.0)).pow(0.5) * (1.0 + 0.0820944379.pow(
    2.0
  ) / 2.0 * (0.5 * ln(
    (1.0 + cos(lat * Math.PI / 180.0) * sin(lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0)) / (1.0 - cos(
      lat * Math.PI / 180.0
    ) * sin(lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0))
  )).pow(2.0) * cos(lat * Math.PI / 180.0).pow(2.0) / 3.0) + 500000.0
  easting = Math.round(easting * 100.0) * 0.01

  var northing: Double = (atan(tan(lat * Math.PI / 180.0) / cos((lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0))) - lat * Math.PI / 180.0) * 0.9996 * 6399593.625 / sqrt(
      1.0 + 0.006739496742 * cos(lat * Math.PI / 180.0).pow(2.0)
    ) * (1.0 + 0.006739496742 / 2.0 * (0.5 * ln(
      (1.0 + cos(lat * Math.PI / 180.0) * sin((lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0))) / (1.0 - cos(
        lat * Math.PI / 180.0
      ) * sin((lon * Math.PI / 180.0 - (6.0 * zone - 183.0) * Math.PI / 180.0)))
    )).pow(2.0) * cos(lat * Math.PI / 180.0).pow(2.0)) + 0.9996 * 6399593.625 * (lat * Math.PI / 180.0 - 0.005054622556 * (lat * Math.PI / 180.0 + sin(
      2.0 * lat * Math.PI / 180.0
    ) / 2.0) + 4.258201531e-05 * (3.0 * (lat * Math.PI / 180.0 + sin(2.0 * lat * Math.PI / 180.0) / 2.0) + sin(2.0 * lat * Math.PI / 180.0) * cos(
      lat * Math.PI / 180.0
    ).pow(2.0)) / 4.0 - 1.674057895e-07 * (5.0 * (3.0 * (lat * Math.PI / 180.0 + sin(2.0 * lat * Math.PI / 180.0) / 2.0) + sin(2.0 * lat * Math.PI / 180.0) * cos(
      lat * Math.PI / 180.0
    ).pow(2.0)) / 4.0 + sin(2.0 * lat * Math.PI / 180.0) * cos(lat * Math.PI / 180.0).pow(2.0) * cos(lat * Math.PI / 180.0).pow(
      2.0
    )) / 3.0)
  if (letter < 'M') northing += 10000000.0
  northing = Math.round(northing * 100.0) * 0.01

  return UTMCoord(zone, letter, easting, northing)
}

fun UTMCoord.convertToLatLon(): LatLonCoord {
  var latitude: Double
  var longitude: Double

  val hem = if (letter > 'M') 'N'.code.toDouble()
  else 'S'.code.toDouble()
  val north = if (hem == 'S'.code.toDouble()) northing - 10000000
  else northing
  latitude =
    (north / 6366197.724 / 0.9996 + (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(2.0) - 0.006739496742 * sin(
      north / 6366197.724 / 0.9996
    ) * cos(north / 6366197.724 / 0.9996) * (atan(
      cos(
        atan(
          (exp(
            (easting - 500000) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
                2.0
              ))
            )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0) / 3.0)
          ) - exp(
            -(easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
                2.0
              ))
            )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0) / 3.0)
          )) / 2.0 / cos(
            (north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3.0 / 4.0 * (north / 6366197.724 / 0.9996 + sin(
              2.0 * north / 6366197.724 / 0.9996
            ) / 2.0) + (0.006739496742 * 3 / 4).pow(2.0) * 5.0 / 3.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(2.0 * north / 6366197.724 / 0.9996) / 2) + sin(
              2.0 * north / 6366197.724 / 0.9996
            ) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 - (0.006739496742 * 3.0 / 4.0).pow(3.0) * 35.0 / 27.0 * (5.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(
              2.0 * north / 6366197.724 / 0.9996
            ) / 2.0) + sin(2.0 * north / 6366197.724 / 0.9996) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 + sin(2.0 * north / 6366197.724 / 0.9996) * cos(
              north / 6366197.724 / 0.9996
            ).pow(2.0) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 3.0)) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0)) + north / 6366197.724 / 0.9996
          )
        )
      ) * tan(
        (north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3.0 / 4.0 * (north / 6366197.724 / 0.9996 + sin(
          2.0 * north / 6366197.724 / 0.9996
        ) / 2.0) + (0.006739496742 * 3.0 / 4.0).pow(2.0) * 5.0 / 3.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(2.0 * north / 6366197.724 / 0.9996) / 2.0) + sin(
          2.0 * north / 6366197.724 / 0.9996
        ) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 - (0.006739496742 * 3.0 / 4.0).pow(3.0) * 35.0 / 27.0 * (5.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(
          2.0 * north / 6366197.724 / 0.9996
        ) / 2.0) + sin(2.0 * north / 6366197.724 / 0.9996) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 + sin(2.0 * north / 6366197.724 / 0.9996) * cos(
          north / 6366197.724 / 0.9996
        ).pow(2.0) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 3.0)) / (0.9996 * 6399593.625 / sqrt(
          (1.0 + 0.006739496742 * cos(
            north / 6366197.724 / 0.9996
          ).pow(2.0))
        )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
          (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
            2.0
          ))
        ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0)) + north / 6366197.724 / 0.9996
      )
    ) - north / 6366197.724 / 0.9996) * 3.0 / 2.0) * (atan(
      cos(
        atan(
          (exp(
            (easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
                2.0
              ))
            )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0) / 3.0)
          ) - exp(
            -(easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
                2.0
              ))
            )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0) / 3.0)
          )) / 2.0 / cos(
            (north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3.0 / 4.0 * (north / 6366197.724 / 0.9996 + sin(
              2.0 * north / 6366197.724 / 0.9996
            ) / 2.0) + (0.006739496742 * 3.0 / 4.0).pow(2.0) * 5.0 / 3.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(2.0 * north / 6366197.724 / 0.9996) / 2.0) + sin(
              2.0 * north / 6366197.724 / 0.9996
            ) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 - (0.006739496742 * 3.0 / 4.0).pow(3.0) * 35.0 / 27.0 * (5.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(
              2.0 * north / 6366197.724 / 0.9996
            ) / 2.0) + sin(2 * north / 6366197.724 / 0.9996) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 + sin(2.0 * north / 6366197.724 / 0.9996) * cos(
              north / 6366197.724 / 0.9996
            ).pow(2.0) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 3.0)) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
              (1.0 + 0.006739496742 * cos(
                north / 6366197.724 / 0.9996
              ).pow(2.0))
            ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0)) + north / 6366197.724 / 0.9996
          )
        )
      ) * tan(
        (north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3.0 / 4.0 * (north / 6366197.724 / 0.9996 + sin(
          2.0 * north / 6366197.724 / 0.9996
        ) / 2.0) + (0.006739496742 * 3.0 / 4.0).pow(2.0) * 5.0 / 3.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(2.0 * north / 6366197.724 / 0.9996) / 2) + sin(
          2.0 * north / 6366197.724 / 0.9996
        ) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 - (0.006739496742 * 3.0 / 4.0).pow(3.0) * 35.0 / 27.0 * (5.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(
          2.0 * north / 6366197.724 / 0.9996
        ) / 2.0) + sin(2.0 * north / 6366197.724 / 0.9996) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 + sin(2.0 * north / 6366197.724 / 0.9996) * cos(
          north / 6366197.724 / 0.9996
        ).pow(2.0) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 3.0)) / (0.9996 * 6399593.625 / sqrt(
          (1.0 + 0.006739496742 * cos(
            north / 6366197.724 / 0.9996
          ).pow(2.0))
        )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
          (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
            2.0
          ))
        ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0)) + north / 6366197.724 / 0.9996
      )
    ) - north / 6366197.724 / 0.9996)) * 180.0 / Math.PI
  latitude = Math.round(latitude * 10000000.0).toDouble()
  latitude /= 10000000.0
  longitude = atan(
    (exp(
      (easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
        (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
          2.0
        ))
      )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
        (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
          2.0
        ))
      ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0) / 3.0)
    ) - exp(
      -(easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
        (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
          2.0
        ))
      )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
        (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
          2.0
        ))
      ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0) / 3.0)
    )) / 2.0 / cos(
      (north - 0.9996 * 6399593.625 * (north / 6366197.724 / 0.9996 - 0.006739496742 * 3.0 / 4.0 * (north / 6366197.724 / 0.9996 + sin(
        2.0 * north / 6366197.724 / 0.9996
      ) / 2.0) + (0.006739496742 * 3 / 4).pow(2.0) * 5.0 / 3.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(2.0 * north / 6366197.724 / 0.9996) / 2.0) + sin(
        2.0 * north / 6366197.724 / 0.9996
      ) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 - (0.006739496742 * 3.0 / 4.0).pow(3.0) * 35.0 / 27.0 * (5.0 * (3.0 * (north / 6366197.724 / 0.9996 + sin(
        2.0 * north / 6366197.724 / 0.9996
      ) / 2.0) + sin(2 * north / 6366197.724 / 0.9996) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 4.0 + sin(2.0 * north / 6366197.724 / 0.9996) * cos(
        north / 6366197.724 / 0.9996
      ).pow(2.0) * cos(north / 6366197.724 / 0.9996).pow(2.0)) / 3.0)) / (0.9996 * 6399593.625 / sqrt(
        (1.0 + 0.006739496742 * cos(
          north / 6366197.724 / 0.9996
        ).pow(2.0))
      )) * (1.0 - 0.006739496742 * ((easting - 500000.0) / (0.9996 * 6399593.625 / sqrt(
        (1.0 + 0.006739496742 * cos(north / 6366197.724 / 0.9996).pow(
          2.0
        ))
      ))).pow(2.0) / 2.0 * cos(north / 6366197.724 / 0.9996).pow(2.0)) + north / 6366197.724 / 0.9996
    )
  ) * 180.0 / Math.PI + zone * 6.0 - 183.0
  longitude = Math.round(longitude * 10000000.0).toDouble()
  longitude /= 10000000.0

  return LatLonCoord(latitude, longitude)
}
