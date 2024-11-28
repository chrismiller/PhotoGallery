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

  var failure: Exception? = null
  val stdoutThread = thread(start = true, name = "stdout") {
    try {
      process.inputStream.bufferedReader().forEachLine(stdOutHandler)
    } catch (e: Exception) {
      failure = e
    }
  }

  val stderrThread = thread(start = true, name = "stderr") {
    process.errorStream.bufferedReader().forEachLine(stdErrHandler)
  }

  stdoutThread.join()
  stderrThread.join()
  val returnCode = process.waitFor()

  failure?.also { throw it }

  if (returnCode != 0) {
    throw RuntimeException("Error while running ${arguments.first()}: $returnCode")
  }

  return 0
}