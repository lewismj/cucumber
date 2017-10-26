package com.waioeka.sbt

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FunSuite, Matchers}


/** non cucumber test, include in set of tests. */
class NonCucumberTest extends FunSuite with Matchers with PropertyChecks {

  test("An empty Set should have size 0") {
    Set.empty.size should be (0)
  }

}