package com.waioeka.sbt.runner

/** Trait to define a test run by the Cucumber runner. */
trait CucumberSpec

/** For suites that want to run in parallel, allow specification of output
  * sub-directories.
  *
  * e.g.
  * json:/tmp/json
  *     /suite1
  *     /suite2
  *     /suite3
  *
  * Will be generated when running in parallel.
  */
trait CucumberTestSuite extends CucumberSpec {
  def features: List[String] = List.empty
  def path: String = ""
}