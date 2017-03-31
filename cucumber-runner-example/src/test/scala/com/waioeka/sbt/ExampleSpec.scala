package com.waioeka.sbt

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FunSuite, Matchers}



class ExampleSpec extends FunSuite with Matchers with PropertyChecks {

  test("An empty Set should have size 0") {
    Set.empty.size should be (0)
  }

}