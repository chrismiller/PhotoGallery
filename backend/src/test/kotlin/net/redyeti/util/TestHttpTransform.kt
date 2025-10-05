package net.redyeti.util

import net.redyeti.gallery.backend.insertUrlLinks
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.FieldSource

class TestHttpTransform {
  companion object {
    val httpArgs = listOf(
      arguments("https://www.google.com ", "{url:https://www.google.com}https://www.google.com{/} "),
      arguments("http://www.google.com ", "{url:http://www.google.com}http://www.google.com{/} "),
      arguments("https://www.google.com", "{url:https://www.google.com}https://www.google.com{/}"),
      arguments(" https://www.google.com", " {url:https://www.google.com}https://www.google.com{/}"),
      arguments("xxxhttps://www.google.com", "xxx{url:https://www.google.com}https://www.google.com{/}"),
      arguments("https://www.google.com\n", "{url:https://www.google.com}https://www.google.com{/}\n"),
      arguments("https://www.google.com\r\n", "{url:https://www.google.com}https://www.google.com{/}\r\n"),
    )
  }

  @ParameterizedTest
  @FieldSource("httpArgs")
  fun testHttpTransform(input: String, output: String) {
    assertEquals(output, insertUrlLinks(input))
  }
}