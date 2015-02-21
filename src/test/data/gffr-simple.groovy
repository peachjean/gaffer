
bind("one", com.example.One) {
	alpha = 1
	beta = 2
	gamma = "fred"
}

bind("two", com.example.Two) {
	name = "jackson"
	size = 42
	oneRef = ref("one")
}
