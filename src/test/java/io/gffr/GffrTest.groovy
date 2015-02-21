package io.gffr

import com.example.One
import com.example.Two
import spock.lang.Specification

import java.nio.file.Paths

/**
 * TODO: Document this class
 */
class GffrTest extends Specification
{
	def "test simple"() {
		given: "a gffr"
			def gffr = new Gffr();
		and: "a simple context"
			def ctx = new SimpleContext()
		when: "i load simple config"
			gffr.loadConfiguredObjects(Paths.get("src", "test", "data", "gffr-simple.groovy"), ctx)
		then: "one has the expected values"
			def one = ctx.loadBindings("one", One)
			one.alpha == 1
			one.beta == 2
			one.gamma == "fred"
		and: "two has the expected values"
			def two = ctx.loadBindings("two", Two)
			two.name == "jackson"
			two.size == 42
			two.oneRef.alpha == 1
			two.oneRef.beta == 2
			two.oneRef.gamma == "fred"

	}
}
