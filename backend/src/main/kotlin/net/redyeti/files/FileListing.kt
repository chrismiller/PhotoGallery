package net.redyeti.files

import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.respondFile
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Route.directoryBrowser(folder: File) {
  val dir = staticRootFolder.combine(folder)
  val pathParameterName = "static-content-path-parameter"
  val dateFormat = SimpleDateFormat("dd-MMM-YYYY HH:mm")
  get("{$pathParameterName...}") {
    val relativePath = call.parameters.getAll(pathParameterName)?.joinToString(File.separator) ?: return@get
    val file = dir.combineSafe(relativePath)

    return@get when {
      !file.exists() -> call.respond(HttpStatusCode.NotFound, null)
      file.isFile -> call.respondFile(file)
      file.isDirectory -> {
        val isRoot = relativePath.trim('/').isEmpty()
        val files = file.listSuspend(includeParent = !isRoot)
        val base = call.request.path().trimEnd('/')
        renderDirectory(base, files, dateFormat)
      }
      else -> call.respond(HttpStatusCode.NotImplemented, null)
    }
  }
}

private suspend fun RoutingContext.renderDirectory(
  base: String,
  files: List<FileInfo>,
  dateFormat: SimpleDateFormat
) {
  call.respondHtml {
    body {
      h1 {
        +"Index of $base/"
      }
      hr {}
      table {
        style = "width: 100%;"
        thead {
          tr {
            for (column in listOf("Name", "Last Modified", "Size", "MimeType")) {
              th {
                style = "width: 25%; text-align: left;"
                +column
              }
            }
          }
        }
        tbody {
          for (finfo in files) {
            val rname = if (finfo.directory) "${finfo.name}/" else finfo.name
            tr {
              td {
                if (finfo.name == "..") {
                  a(File(base).parent) { +rname }
                } else {
                  a("$base/$rname") { +rname }
                }
              }
              td {
                +dateFormat.format(finfo.date)
              }
              td {
                +(if (finfo.directory) "-" else "${finfo.size}")
              }
              td {
                +(ContentType.fromFilePath(finfo.name).firstOrNull()?.toString() ?: "-")
              }
            }
          }
        }
      }
      hr {}
    }
  }
}

private fun File?.combine(file: File) = when {
  this == null -> file
  else -> resolve(file)
}

data class FileInfo(val name: String, val date: Date, val directory: Boolean, val size: Long)

suspend fun File.listSuspend(includeParent: Boolean = false): List<FileInfo> {
  val file = this
  return withContext(Dispatchers.IO) {
    listOfNotNull(if (includeParent) FileInfo("..", Date(), true, 0L) else null) + file.listFiles().toList().map {
      FileInfo(it.name, Date(it.lastModified()), it.isDirectory, it.length())
    }.sortedWith(
      comparators(
        Comparator { a, b -> -a.directory.compareTo(b.directory) },
        Comparator { a, b -> a.name.compareTo(b.name, ignoreCase = true) }
      )
    )
  }
}

fun <T> comparators(vararg comparators: Comparator<T>): Comparator<T> {
  return Comparator { l, r ->
    for (comparator in comparators) {
      val result = comparator.compare(l, r)
      if (result != 0) return@Comparator result
    }
    return@Comparator 0
  }
}

operator fun <T> Comparator<T>.plus(other: Comparator<T>): Comparator<T> = comparators(this, other)