package net.redyeti.util

import java.io.Reader

class CsvParser(val delimiter: Char = ',', val quoteChar: Char = '"', val escapeChar: Char = '\\') {
  private var lineNumber = 0
  private var columnNumber = 0

  private var escaped = false

  private enum class State {
    Start,
    End,
    Field,
    QuotedField
  }

  fun parseLine(chars: Reader, headers: List<String>): Map<String, String> {
    val fields = parseLine(chars)
    val result = mutableMapOf<String, String>()
    fields.forEachIndexed { i, value ->
      result[headers[i]] = value
    }
    return result
  }

  fun parseLine(chars: Reader): List<String> {
    val result = mutableListOf<String>()
    val field = StringBuilder()
    var state = State.Start

    fun endField() {
      result += field.toString()
      field.clear()
    }

    while (true) {
      val cint = chars.read()
      if (cint == -1) {
        endField()
        break
      }
      val ch = cint.toChar()
      columnNumber++

      when (ch) {
        quoteChar -> {
          if (escaped) {
            escaped = false
          } else {
            when (state) {
              State.Start -> {
                state = State.QuotedField
                continue
              }
              State.QuotedField -> {
                state = State.End
                continue
              }
              State.End -> {
                state = State.QuotedField
              }
              else -> throw IllegalStateException("Unexpected quote character at line $lineNumber, col $columnNumber")
            }
          }
        }
        escapeChar -> {
          if (state == State.End) throw IllegalStateException("Unexpected escape character at line $lineNumber, col $columnNumber")
          escaped = !escaped
          if (escaped) continue
        }
        delimiter -> {
          if (state == State.End || state == State.Start || state == State.Field) {
            endField()
            state = State.Start
            continue
          }
        }
        '\r' -> {
          continue
        }
        '\n' -> {
          if (state == State.QuotedField) throw IllegalStateException("Missing end quote at line $lineNumber, col $columnNumber")
          endField()
          break
        }
        else -> {
          if (state == State.Start) {
            state = State.Field
          }
        }
      }
      if (state == State.End) throw IllegalStateException("Unexpected '$ch' at line $lineNumber, col $columnNumber")
      escaped = false
      field.append(ch)
    }
    lineNumber++
    columnNumber = 0
    return result
  }
}