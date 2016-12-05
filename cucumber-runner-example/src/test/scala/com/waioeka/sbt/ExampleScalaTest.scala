package com.waioeka.sbt

import org.scalatest.{FlatSpec, Matchers}
import scala.collection.mutable

/**
  * Example included to ensure that other frameworks will
  * run alongside Cucumber tests when 'sbt test' is invoked.
  */
class ExampleScalaTest extends FlatSpec with Matchers {
  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new mutable.Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new mutable.Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }

}
