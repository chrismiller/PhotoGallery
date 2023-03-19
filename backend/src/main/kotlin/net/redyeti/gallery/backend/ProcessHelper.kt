package net.redyeti.gallery.backend

import java.nio.file.Path
import kotlin.concurrent.thread

fun execute(
  arguments: List<String>,
  stdOutHandler: (String) -> Unit = {},
  stdErrHandler: (String) -> Unit = {},
  workingDir: Path = Path.of(".")
): Int {
  println("$workingDir    ${arguments.joinToString(" ")}")
  val process = ProcessBuilder(arguments)
    .apply {
      directory(workingDir.toFile())
      redirectOutput(ProcessBuilder.Redirect.PIPE)
      redirectError(ProcessBuilder.Redirect.PIPE)
    }
    .start()

  thread(start = true, name = "stdout") {
    process.inputStream.bufferedReader().forEachLine(stdOutHandler)
  }

  thread(start = true, name = "stderr") {
    process.errorStream.bufferedReader().forEachLine(stdErrHandler)
  }

  return process.waitFor()
}