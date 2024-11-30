package net.redyeti.util

import java.io.Reader

class CsvParser<H>(val delimiter: Char = ',', val quoteChar: Char = '"', val escapeChar: Char = '\\') {
  private var lineNumber = 0
  private var columnNumber = 0

  private var escaped = false

  private enum class State {
    Start,
    End,
    Field,
    QuotedField
  }

  fun parseLine(chars: Reader, headers: List<H?>): Map<H, String> {
    val fields = parseLine(chars)
    val result = mutableMapOf<H, String>()
    fields.forEachIndexed { i, value ->
      headers[i]?.apply { result[this] = value }
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
          // If we're within a quoted field, treat this as a multiline string
          if (state == State.QuotedField) {
            field.append(ch)
            lineNumber++
            columnNumber = 0
            continue
          } else {
            endField()
            break
          }
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