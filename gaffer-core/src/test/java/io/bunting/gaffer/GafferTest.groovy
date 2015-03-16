package io.bunting.gaffer

import spock.lang.Specification

import java.nio.file.Paths

/**
 * TODO: Document this class
 */
class GafferTest extends Specification {
  def "test simple"() {
    given: "a gffr"
      def gfrLocation = Paths.get("src", "test", "data", "gaffer-simple.groovy")
      def gaffer = new SimpleGaffer(new PathConfigReader(gfrLocation), ExampleGoal);
    when: "i load simple config"
      def goal = gaffer.load()
    then: "one has the expected values"
      def one = goal.one
      one.alpha == 1
      one.beta == 2
      one.gamma == "fred"
    and: "two has the expected values"
      def two = goal.two
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
