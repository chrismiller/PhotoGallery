# Compose HTML Style Sheets

### Examples:

```kotlin
// --------------------------------------------
// .x { ... }
// --------------------------------------------
val x by style { ... }

// --------------------------------------------
// https://www.w3.org/TR/selectors/#compound
// .x.y { ... }
// --------------------------------------------
val y by style {}
val x by style {
  self + className(y) style { ... }
}

// --------------------------------------------
// https://www.w3.org/TR/selectors/#descendant-combinators
// .x .y { ... }
// --------------------------------------------
val y by style {}
val x by style {
  className(y) style { ... }
}
// or
val x by style {
  ".y" style { ... }
}

// --------------------------------------------
// https://www.w3.org/TR/selectors/#grouping
// .x, .y { ... }
// --------------------------------------------
val y by style {}
val x by style {
  group(self, y) style { ... }
}

// --------------------------------------------
// https://www.w3.org/TR/selectors/#grouping
// .x .y, .x .z { ... }
// --------------------------------------------
val y by style {}
val z by style {}
val x by style {
  group(desc(self, className(y)), desc(self, className(z))) style { ... }
}

// --------------------------------------------
// img { ... }
// --------------------------------------------
init {
  "img" style { ... }
}


// --------------------------------------------
// img .x { ... }
// --------------------------------------------
val x by style {}
init {
  desc("img", className(x)) style { ... }
  // or
  "img" style {
    className(x) style { ... }
  }
}


// --------------------------------------------
// img.x { ... }
// --------------------------------------------
val x by style {}
init {
  "img" style {
    self + className(x) style { ... }
    // or
    combine(selector("img"), className(x)) style { ... }
  }
}


// --------------------------------------------
// .x::before { ... }
// --------------------------------------------
val x by style {
  self + before style { ... }
}


// --------------------------------------------
// .x::before, .x::after { ... }
// --------------------------------------------
val x by style {
  group(self + before, self + after) style { ... }
}


// --------------------------------------------
// @media "screen and ..." {
//   .x { ... }
// }
// --------------------------------------------
val x by style {
  media("screen and ...") {
    self style { ... }
  }
}
// --------------------------------------------
// https://www.w3.org/TR/selectors/#child-combinators
// x > y { ... }
// --------------------------------------------
child(x, y) style { ... }


// --------------------------------------------
// https://www.w3.org/TR/selectors/#general-sibling-combinators
// x ~ y { ... }
// --------------------------------------------
sibling(x, y) style { ... }


// --------------------------------------------
// https://www.w3.org/TR/selectors/#adjacent-sibling-combinators
// x + y { ... }
// --------------------------------------------
adjacent(x, y) style { ... }


// --------------------------------------------
// * { ... }
// --------------------------------------------
init {
  universal style { ... }
}
```
