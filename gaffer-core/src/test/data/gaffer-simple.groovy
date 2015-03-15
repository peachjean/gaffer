import javax.inject.Named

bind("one", com.example.One) {
  alpha = 1
  beta = 2
  gamma = "fred"
}

bind("two", com.example.Two) {
  name = "jackson"
  size = 42
  oneRef = ref("one")
  oneNested = build(com.example.One) {
    alpha = 6
    beta = 7
    gamma = "albert"
  }
}

