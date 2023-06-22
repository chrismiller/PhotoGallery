# Compose HTML Style Sheets

### Examples:

```kotlin
// --------------------------------------------
// .a { ... }
val a by style { ... }

// --------------------------------------------
// .a .b { ... }
val b by style {}
val a by style {
  className(b) style { ... }
}

// --------------------------------------------
// .a, .b { ... }
val b by style {}
val a by style {
  group(b) style { ... }
}

// --------------------------------------------
// img { ... }
init {
  "img" style { ... }
}

// --------------------------------------------
// img .a { ... }
val a by style {}
init {
  desc("img", className(a)) style { ... }
}

// --------------------------------------------
// img.a { ... }
val a by style {}
init {
  "img" style {
    combine(selector("img"), className(a)) style { ... }
  }
}

// --------------------------------------------
// .a::after { ... }
val a by style {
  self + after style { ... }
}

// --------------------------------------------
// .a::before, .a::after { ... }
val a by style {
  group(self + before, self + after) style { ... }
}

// --------------------------------------------
// @media "screen and ..." {
//   .a { ... }
// }
val a by style {
  media("screen and ...") {
    self style { ... }
  }
}

// --------------------------------------------
// a > b { ... }
child(a, b) style { ... }

// --------------------------------------------
// a ~ b { ... }
sibling(a, b) style { ... }

// --------------------------------------------
// a + b { ... }
adjacent(a, b) style { ... }

// --------------------------------------------
// * { ... }
init {
  universal style { ... }
}
```
