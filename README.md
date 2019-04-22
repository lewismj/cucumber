# Cucumber Test Framework & Plugin for SBT
<p align="left">
<a href="https://travis-ci.org/lewismj/cucumber">
<img src="https://travis-ci.org/lewismj/cucumber.svg?branch=master"/>
</a>
<a href="https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22cucumber-runner_2.12%22">
<img src="https://maven-badges.herokuapp.com/maven-central/com.waioeka.sbt/cucumber-runner_2.12/badge.svg"/>
</a>
<a href="https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22cucumber-plugin%22">
<img src="https://maven-badges.herokuapp.com/maven-central/com.waioeka.sbt/cucumber-plugin/badge.svg"/>
</a>
<a href="https://waffle.io/lewismj/cucumber">
<img src="https://badge.waffle.io/lewismj/cucumber.svg?columns=In%20Progress,Done&style=flat-square">
</a>
<a href="https://gitter.im/lewismj/cucumber">
<img src="https://badges.gitter.im/Join%20Chat.svg">
</a>
</p>

## Update Notes 


1. The runner (0.2.0) now supports running test suites in parallel.

    * The existing method of running everything serially and producing a consolidated report is still supported.
    
    * A new method of defining suites that can run in parallel is introduced.
    
    Described below, project examples of both approaches are [here](https://github.com/lewismj/cucumber/tree/master/examples).

2. The plugin has moved and is now available within the [sbt github](https://github.com/sbt/sbt-cucumber).

## Summary

This project contains a Cucumber test framework for sbt. There is also a plugin that provides a new sbt command.

1. **_Cucumber Test Framework_**  An sbt test framework, the _runner_, runs Cucumber tests as part of a unit test run (i.e. `sbt test`).

2.  An **SBT [plugin](plugin.md)** that provides a new command `sbt cucumber`. It allows you to run Cucumber tests independently of unit tests.
  

Unless you have a specific requirement to run Cucumber tests outside of unit test framework, use the test framework
rather than the plugin.

The plugin can be used if you want a separate command to run Cucumber tests and have your normal test framework
ignore Cucumber tests. If you want to use the plugin, there is more information [here](plugin.md).

__The Cucumber test framework does not depend on the plugin__.

## Dependency Information

```scala
libraryDependencies += "com.waioeka.sbt" %% "cucumber-runner" % "0.2.0"
```

## Contact

Michael Lewis: lewismj@mac.com


## Cucumber Test Framework

You can run tests two ways:

1. Run tests in parallel. Each test suite must mixin `CucumberTestSuite` and 
   overload two methods, that tell the suite which are the corresponding features to run
   and which sub-directory of the plugin output to put the results.

2. Sequentially, this runs all Cucumber tests serially and produces a consolidated output,
   i.e. a single html or json (choose the plugin(s) in your build.sbt file.)


## 1. Running tests in parallel.

Define a suite that supports a number of features, for example,

```scala
class AddAndMultiplySteps extends ScalaDsl with EN with Matchers with CucumberTestSuite  {
  override def features = List("Multiplication.feature","Addition.feature")
  override def path = "addAndMult" // this is the output path, appended to plugin path.

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

  When("""^I add x \+ y$"""){ () =>
    z = x + y
  }

   Then("""^I get (\d+)$""") { (arg0: Int) =>
     z should be (arg0)
   }
}
```

In your build.sbt switch on the parallel output:

```scala
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

// Configure the arguments.
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")

/** can remove pretty printing if running in parallel. */
parallelExecution in Test := true
```

At present, the output for each suite will be a sub-directory of the plugin output.


## 2. Running all the features serially to produce a consolidated test report.


In your test project define an empty class that inherits from `Cucumber Spec`

```scala
class Spec extends CucumberSpec
```

Add the following properties to `build.sbt`:

```scala
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

// Configure the arguments.
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")
parallelExecution in Test := false 
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


/** AddAndMultiplySteps*/
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
