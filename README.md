# Cucumber Test Framework & Plugin for SBT

<p align="left">
<!--<img src="https://travis-ci.org/lewismj/cucumber.svg?branch=master"/>-->
<img src="https://maven-badges.herokuapp.com/maven-central/com.waioeka.sbt/cucumber-runner_2.12/badge.svg"/>
</p>

## Update Notes

Cucumber now builds for 2.12 Scala, latest release is an update for Scala 2.12 (compatible version is 0.1.2).

Note, Until 2.0.0 Cucumber is released, we have to use the SNAPSHOT version for Scala 2.12 compatibility, i.e. use: `resolvers += Resolver.sonatypeRepo("snapshots")`



## Summary

This project contains a Cucumber test framework for sbt. There is also a plugin that provides a new sbt command.

1. **_Cucumber Test Framework_**  An sbt test framework, the _runner_, runs Cucumber tests as part of a unit test run (i.e. `sbt test`).

2.  A [plugin](plugin.md)  provides a new command `sbt cucumber`. It allows you to run Cucumber tests independently of unit tests.
  

Unless you have a specific requirement to run Cucumber tests outside of unit test framework, use the test framework
rather than the plugin.

The plugin can be used if you want a separate command to run Cucumber tests and have your normal test framework
ignore Cucumber tests. If you want to use the plugin, there is more information [here](plugin.md).

_The Cucumber test framework does not depend on the plugin_.

## Issues

Waffle board [here][1]

## Dependency Information

```scala
libraryDependencies += "com.waioeka.sbt" %% "cucumber-runner" % "0.0.8"
```

## Notes


**0.0.8+** Cucumber Test Framework (_runner_)

1. **(n.b. change from previous versions)**  Use **CucumberSpec** as the base class for your Cucumber test suite now. See the cucumber runner example.

2. You can specify the Cucumber arguments via your `build.sbt` file, as follows:

```scala
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

testOptions in Test += Tests.Argument(framework,"--monochrome")
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")
```

In your class definition, use:

```scala
class MyCucumberTestSuite extends CucumberSpec
```

## Contact

Michael Lewis: lewismj@waioeka.com

## Cucumber Test Framework

To use the Cucumber test framework, update your `build.sbt` to include the new framework and
specify the test options. e.g.

```scala
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

// Configure the arguments.
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")
```

The framework will expect feature files in the `test/resources` directory. If your feature files are stored elsewhere, add that location to the 'unmanagedClasspath', e.g.

```scala
unmanagedClasspath in Test += baseDirectory.value / "src/test/features"
```


### Example

The project _example_ illustrates how to setup and use the _runner_. To integrate BDD testing into your unit test framework.
As shown below, using the runner and plugin, you can now run `sbt test`.

```scala
package com.waioeka.sbt

import com.waioeka.sbt.runner.CucumberSpec
import cucumber.api.scala.{ScalaDsl, EN}
import org.scalatest.Matchers

class CucumberTestSuite extends CucumberSpec


/** MultiplicationSteps */
class MultiplicationSteps extends ScalaDsl with EN with Matchers  {
  var x : Int = 0
  var y : Int = 0
  var z : Int = 0

  Given("""^a variable x with value (\d+)$""") { (arg0: Int) =>
    x = arg0
  }

  Given("""^a variable y with value (\d+)$""") { (arg0: Int) =>
    y = arg0
  }

  When("""^I multiply x \* y$""") { () =>
    z = x * y
  }

 Then("""^I get (\d+)$""") { (arg0: Int) =>
   z should be (arg0)
 }
}
```


```
> test
[info] ExampleSpec:
[info] - An empty Set should have size 0
@my-test
Feature: Multiplication
  In order to avoid making mistakes
  As a dummy
  I want to multiply numbers

  Scenario: Multiply two variables  # Multiplication.feature:7
    Given a variable x with value 2 # MultiplicationSteps.scala:44
    And a variable y with value 3   # MultiplicationSteps.scala:48
    When I multiply x * y           # MultiplicationSteps.scala:52
    Then I get 6                    # MultiplicationSteps.scala:56

1 Scenarios (1 passed)
4 Steps (4 passed)
0m0.081s


1 Scenarios (1 passed)
4 Steps (4 passed)
0m0.081s

[info] CucumberTestSuite .. passed
[info] ScalaTest
[info] Run completed in 422 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] CucumberTest
[info] Tests: succeeded 1, failed 0
[info] Passed: Total 2, Failed 0, Errors 0, Passed 2
[success] Total time: 1 s, completed 02-Apr-2017 23:16:53
>
```

[1]:	https://waffle.io/lewismj/cucumber
