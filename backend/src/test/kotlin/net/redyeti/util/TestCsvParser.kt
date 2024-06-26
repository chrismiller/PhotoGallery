package net.redyeti.util

import kotlin.test.Test
import kotlin.test.assertEquals

class TestCsvParser {
  @Test
  fun testSimple() {
    val parser = CsvParser()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine("abc,123,def".reader()))
  }

  @Test
  fun testNewlines() {
    val parser = CsvParser()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine("abc,123,def\r\n".reader()))
    assertEquals(listOf("abc", "123", "def"), parser.parseLine("abc,123,def\n\r".reader()))
    assertEquals(listOf("abc", "123", "def"), parser.parseLine("abc,123,def\n".reader()))

    var reader = "abc,123,def\nxyz".reader()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine(reader))
    assertEquals(listOf("xyz"), parser.parseLine(reader))

    reader = "abc,123,def\r\nxyz\n".reader()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine(reader))
    assertEquals(listOf("xyz"), parser.parseLine(reader))

    reader = "abc,123,def\n\rxyz\r\n".reader()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine(reader))
    assertEquals(listOf("xyz"), parser.parseLine(reader))
  }

  @Test
  fun testMultilineField() {
    val parser = CsvParser()
    assertEquals(listOf("a", "b\nc", "d"), parser.parseLine("a,\"b\nc\",d".reader()))
  }

  @Test
  fun testEscaping() {
    val parser = CsvParser()

    val reader = "\\abc,1\\23,def\nx\\\\yz".reader()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine(reader))
    assertEquals(listOf("x\\yz"), parser.parseLine(reader))
  }

  @Test
  fun testQuotes() {
    val parser = CsvParser()

    var reader = "abc,\"123\",def".reader()
    assertEquals(listOf("abc", "123", "def"), parser.parseLine(reader))

    reader = "abc,\"12,3\",def".reader()
    assertEquals(listOf("abc", "12,3", "def"), parser.parseLine(reader))

    reader = "abc,\"12,3\",def\nabc,\"12,3\",def".reader()
    assertEquals(listOf("abc", "12,3", "def"), parser.parseLine(reader))
    assertEquals(listOf("abc", "12,3", "def"), parser.parseLine(reader))

    reader = "abc,\"12\"\"3\",def".reader()
    assertEquals(listOf("abc", "12\"3", "def"), parser.parseLine(reader))
  }
}