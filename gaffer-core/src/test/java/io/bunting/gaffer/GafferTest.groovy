package io.bunting.gaffer

import com.example.One
import com.example.Two
import io.bunting.gaffer.status.OnConsoleStatusListener
import spock.lang.Specification

import java.nio.file.Paths

/**
 * TODO: Document this class
 */
class GafferTest extends Specification {
  def "test simple"() {
    given: "a gffr"
    def gfrLocation = Paths.get("src", "test", "data", "gaffer-simple.groovy")
    def gffr = new Gaffer(gfrLocation);
    and: "a simple context"
    def ctx = new SimpleContext()
    OnConsoleStatusListener.addNewInstanceToContext(ctx)
    when: "i load simple config"
    gffr.loadConfiguredObjects(ctx)
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
    two.oneNested.alpha == 6
    two.oneNested.beta == 7
    two.oneNested.gamma == "albert"

  }
}
